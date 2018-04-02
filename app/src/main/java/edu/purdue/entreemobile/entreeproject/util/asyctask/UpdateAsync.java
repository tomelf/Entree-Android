package edu.purdue.entreemobile.entreeproject.util.asyctask;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by kane on 3/9/15.
 */
public class UpdateAsync extends AsyncTask<Void, Void, Void> {

    Context context;

    public UpdateAsync(Context rootContext) {
        this.context = rootContext;

        beforeUpdate();
    }

    @Override
    protected Void doInBackground(Void... params) {
        onUpdate();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        afterUpdate();
    }

    protected void beforeUpdate() { return; }

    protected void afterUpdate() { return; }

    protected void onUpdate() { return; }

    protected Context getCurrentContext() {
        return this.context;
    }
}