package com.jjc.qiqiharuniversity;

import android.util.Log;

import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.LogHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJsoup() {
//        String html = "<html><head><title>First parse</title></head>"
//                + "<body><p id='ppp'>Parsed HTML into a doc.</p></body></html>";
//        Document doc = Jsoup.parse(html);
//        System.out.println(doc.getElementById("ppp").text());

//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                Document document = null;
//                String url = "https://www.baidu.com";
//                try {
//                    document = Jsoup.connect(url).get();
//                    e.onNext(String.valueOf(document));
//                } catch (IOException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull String s) {
//                Log.d("document: ", s);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                String url = "https://www.baidu.com";
                try {
                    document = Jsoup.connect(url).get();
                    System.out.println( "jsoup:" + document.body().toString());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }).start();

    }
}