package com.strongloop.android.loopback;

import android.content.Context;

import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.BeanUtil;

import org.apache.http.client.HttpResponseException;

/**
 * Created by Chaitanya on 29-06-2015.
 */
public class Installation extends LocalInstallation {

    private final RestAdapter loopbackAdapter;

    private String deviceId;
    private String userName;

    /**
     * Creates a new instance of LocalInstallation class. Your application
     * should never instantiate more than one instance.
     *
     * @param applicationContext The Android context of your application.
     * @param loopbackAdapter    The adapter to use for communication with
     */
    public Installation(Context applicationContext, RestAdapter loopbackAdapter) {
        super(applicationContext, loopbackAdapter);
        this.loopbackAdapter = loopbackAdapter;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Saves the Installation to the remote server. When called for the first
     * time, a new record is created and the id is stored in SharedPreferences.
     * Subsequent calls updates the existing record with the stored id.
     * @param callback The callback to be executed when finished.
     */
    public void save(final VoidCallback callback) {
        ModelRepository<Model> repository =
                loopbackAdapter.createRepository("installation");
        final Model model = repository.createModel(
                BeanUtil.getProperties(this, true, false));

        model.save(new VoidCallback() {
            @Override
            public void onSuccess() {
                Object id = model.getId();
                Installation.this.setId(id);
                saveInstallationId();
                callback.onSuccess();
            }

            @Override
            public void onError(Throwable t) {
                if (t instanceof HttpResponseException) {
                    HttpResponseException ex = (HttpResponseException) t;
                    if (ex.getStatusCode() == 404 && getId() != null) {
                        // Our Installation ID is no longer valid.
                        // Try to create a new installation instead.
                        setId(null);
                        save(callback);
                        return;
                    }
                }
                callback.onError(t);
            }
        });
    }
}
