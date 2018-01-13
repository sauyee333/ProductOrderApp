package tk.ckknight.productqueue.network.rest.ckKnight;

/**
 * Created by sauyee on 13/1/18.
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);

    void onError(String httpErrorCode, String httpErrorMsg, String responseMsg);
}
