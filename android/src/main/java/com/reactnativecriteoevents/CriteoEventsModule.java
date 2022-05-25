// CriteoEventsModule.java
package com.reactnativecriteoevents;

import androidx.annotation.NonNull;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.criteo.events.TransactionConfirmationEvent;
import com.criteo.events.product.BasketProduct;
import com.criteo.events.product.Product;
import com.criteo.events.AppLaunchEvent;
import com.criteo.events.EventService;
import com.criteo.events.ProductListViewEvent;
import com.criteo.events.ProductViewEvent;
import com.criteo.events.BasketViewEvent;
import com.criteo.events.DeeplinkEvent;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.util.ArrayList;
import java.util.Currency;

@ReactModule(name = CriteoEventsModule.NAME)
public class CriteoEventsModule extends ReactContextBaseJavaModule {
    public static final String NAME = "CriteoEvents";
    private final ReactApplicationContext reactContext;
    public static EventService initCriteoEvent = null;

    public CriteoEventsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void init(String accountName, String countryCode, String language, String email) {
        initCriteoEvent = new EventService(reactContext);
        initCriteoEvent.setAccountName(accountName);
        initCriteoEvent.setCountry(countryCode);
        initCriteoEvent.setLanguage(language);
        initCriteoEvent.setEmail(email);
        Log.i("CriteoLogin",initCriteoEvent.getAccountName());
    }

    /** Send app launch event to criteo **/
    @ReactMethod
    public void launch() {
        if (initCriteoEvent != null) {
          AppLaunchEvent appLaunch = new AppLaunchEvent();
          initCriteoEvent.send(appLaunch);
        }
    }

    /** Send product detail view event to criteo **/
    @ReactMethod
    public void productView(String identifier, double price, String currency) {
        if (initCriteoEvent != null) {
          ProductViewEvent productViewEvent = new ProductViewEvent(identifier, price);
          productViewEvent.setCurrency(Currency.getInstance(currency));
          initCriteoEvent.send(productViewEvent);
        }
    }

    @ReactMethod
    public void deepLinkEvent(String url) {
        if (initCriteoEvent != null) {
          DeeplinkEvent deeplinkEvent  = new DeeplinkEvent(url);
          initCriteoEvent.send(deeplinkEvent);
        }
    }

    /** Send add basket event to criteo **/
    @ReactMethod
    public void basketProduct(String productId, double price, int quantity){
      if(initCriteoEvent!=null){
          BasketProduct product = new BasketProduct(productId,price,quantity);
          BasketViewEvent basketViewEvent = new BasketViewEvent(product);
          initCriteoEvent.send(basketViewEvent);
      }
    }

  /** Send purchase event to criteo **/
  @ReactMethod
  public void purchase(String orderId, String productId, double price, int quantity) {
    if (initCriteoEvent != null) {
      BasketProduct basketProduct = new BasketProduct(productId, price, quantity);
      TransactionConfirmationEvent transactionConfirmationEvent = new TransactionConfirmationEvent(orderId,
        basketProduct);
      initCriteoEvent.send(transactionConfirmationEvent);
    }
  }

  /** Send product list view event to criteo **/
  @ReactMethod
  public void productListView(ReadableArray list, double price, String currency) {
      if (initCriteoEvent != null && list != null) {
        ArrayList<Product> productList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
          String id = list.getString(i);
          Product product = new Product(id, price);
          productList.add(product);
        }

        ProductListViewEvent productListViewEvent = new ProductListViewEvent(productList);
        productListViewEvent.setCurrency(Currency.getInstance(currency));
        initCriteoEvent.send(productListViewEvent);
      }
  }
  
  
  private void alertView( String message ) {
   AlertDialog.Builder dialog = new AlertDialog.Builder(reactContext);
   dialog.setTitle( "Hello" )
         .setMessage(message)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i) {   
          }               
          }).show();
   }  


}
