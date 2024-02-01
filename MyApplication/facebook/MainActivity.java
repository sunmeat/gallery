package com.sunmeat.facebook;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class MainActivity extends FragmentActivity {

    private final String PENDING_ACTION_BUNDLE_KEY = "com.sunmeat.facebook:PendingAction";

    private Button postStatusUpdateButton;
    private Button postPhotoButton;
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private final FacebookCallback<Sharer.Result> shareCallback =
            new FacebookCallback<Sharer.Result>() {
                @Override
                public void onCancel() {
                    Log.d("HelloFacebook", "Отменено");
                }

                @Override
                public void onError(@NonNull FacebookException error) {
                    Log.d("HelloFacebook", String.format("Ошибка: %s", error.toString()));
                    String title = getString(R.string.error);
                    String alertMessage = error.getMessage();
                    showResult(title, alertMessage);
                }

                @Override
                public void onSuccess(@NonNull Sharer.Result result) {
                    Log.d("HelloFacebook", "Всё ок!");
                    if (result.getPostId() != null) {
                        String title = getString(R.string.success);
                        String id = result.getPostId();
                        String alertMessage = getString(R.string.successfully_posted_post, id);
                        showResult(title, alertMessage);
                    }
                }

                private void showResult(String title, String alertMessage) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(title)
                            .setMessage(alertMessage)
                            .setPositiveButton(R.string.ok, null)
                            .show();
                }
            };

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance()
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(@NonNull LoginResult loginResult) {
                                handlePendingAction();
                                updateUI();
                            }

                            @Override
                            public void onCancel() {
                                if (pendingAction != PendingAction.NONE) {
                                    showAlert();
                                    pendingAction = PendingAction.NONE;
                                }
                                updateUI();
                            }

                            @Override
                            public void onError(@NonNull FacebookException exception) {
                                if (pendingAction != PendingAction.NONE
                                        && exception instanceof FacebookAuthorizationException) {
                                    showAlert();
                                    pendingAction = PendingAction.NONE;
                                }
                                updateUI();
                            }

                            private void showAlert() {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle(R.string.cancelled)
                                        .setMessage(R.string.permission_not_granted)
                                        .setPositiveButton(R.string.ok, null)
                                        .show();
                            }
                        });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.activity_main);

        profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        updateUI();
                        handlePendingAction();
                    }
                };

        profilePictureView = findViewById(R.id.profilePicture);
        greeting = findViewById(R.id.greeting);

        postStatusUpdateButton = findViewById(R.id.postStatusUpdateButton);
        postStatusUpdateButton.setOnClickListener(
                view -> onClickPostStatusUpdate());

        postPhotoButton = findViewById(R.id.postPhotoButton);
        postPhotoButton.setOnClickListener(
                view -> onClickPostPhoto());

        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // обработка результатов выбора изображения
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            postSelectedPhoto(selectedImageUri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        LoginManager.getInstance().unregisterCallback(callbackManager);
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.isCurrentAccessTokenActive();

        postStatusUpdateButton.setEnabled(enableButtons || canPresentShareDialog);
        postPhotoButton.setEnabled(enableButtons || canPresentShareDialogWithPhotos);

        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            profilePictureView.setProfileId(profile.getId());
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private void onClickPostStatusUpdate() {
        performPublish(canPresentShareDialog);
    }

    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent =
                new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://www.google.com/"))
                        .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private void onClickPostPhoto() {
        // интент для выбора изображения из галереи
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void postPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    private void postSelectedPhoto(Uri selectedImageUri) {
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setImageUrl(selectedImageUri)
                .setCaption("Your photo caption")
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();

        ShareDialog.show(this, sharePhotoContent);
    }

    private boolean hasPublishPermission() {
        return AccessToken.isCurrentAccessTokenActive()
                && AccessToken.getCurrentAccessToken().getPermissions().contains("publish_actions");
    }

    private void performPublish(boolean allowNoToken) {
        if (AccessToken.isCurrentAccessTokenActive() || allowNoToken) {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
            handlePendingAction();
        }
    }

    private void showResult(String title, String alertMessage) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}