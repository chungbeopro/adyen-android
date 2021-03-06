/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by caiof on 23/7/2019.
 */

package com.adyen.checkout.card;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.adyen.checkout.base.ComponentAvailableCallback;
import com.adyen.checkout.base.StoredPaymentComponentProvider;
import com.adyen.checkout.base.component.lifecycle.PaymentComponentViewModelFactory;
import com.adyen.checkout.base.model.paymentmethods.PaymentMethod;
import com.adyen.checkout.base.model.paymentmethods.StoredPaymentMethod;
import com.adyen.checkout.card.data.CardType;
import com.adyen.checkout.core.log.LogUtil;
import com.adyen.checkout.core.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class CardComponentProvider implements StoredPaymentComponentProvider<CardComponent, CardConfiguration> {
    private static final String TAG = LogUtil.getTag();

    @SuppressWarnings("LambdaLast")
    @Override
    @NonNull
    public CardComponent get(
            @NonNull ViewModelStoreOwner viewModelStoreOwner,
            @NonNull PaymentMethod paymentMethod,
            @NonNull CardConfiguration configuration) {
        final CardConfiguration verifiedConfiguration = checkSupportedCardTypes(paymentMethod, configuration);
        final PaymentComponentViewModelFactory factory = new PaymentComponentViewModelFactory(new CardDelegate(paymentMethod), verifiedConfiguration);
        return new ViewModelProvider(viewModelStoreOwner, factory).get(CardComponent.class);
    }

    @SuppressWarnings("LambdaLast")
    @Override
    @NonNull
    public CardComponent get(
            @NonNull ViewModelStoreOwner viewModelStoreOwner,
            @NonNull StoredPaymentMethod storedPaymentMethod,
            @NonNull CardConfiguration configuration) {
        final PaymentComponentViewModelFactory factory =
                new PaymentComponentViewModelFactory(new StoredCardDelegate(storedPaymentMethod), configuration);
        return new ViewModelProvider(viewModelStoreOwner, factory).get(CardComponent.class);
    }

    @Override
    public void isAvailable(
            @NonNull Application applicationContext,
            @NonNull PaymentMethod paymentMethod,
            @NonNull CardConfiguration configuration,
            @NonNull ComponentAvailableCallback<CardConfiguration> callback) {

        final boolean isPubKeyAvailable = !TextUtils.isEmpty(configuration.getPublicKey());
        callback.onAvailabilityResult(isPubKeyAvailable, paymentMethod, configuration);
    }

    /**
     * Check which set of supported cards to pass to the component.
     * Priority is: Custom -> PaymentMethod.brands -> Default
     *
     * @param paymentMethod The payment methods object that will start the component.
     * @param cardConfiguration The configuration object that will start the component.
     * @return The Configuration object with possibly adjusted values.
     */
    private CardConfiguration checkSupportedCardTypes(@NonNull PaymentMethod paymentMethod, @NonNull CardConfiguration cardConfiguration) {
        if (cardConfiguration.getSupportedCardTypes().isEmpty()) {
            final List<String> brands = paymentMethod.getBrands();

            List<CardType> supportedCardTypes = CardConfiguration.DEFAULT_SUPPORTED_CARDS_LIST;

            // Get card types from brands in PaymentMethod object
            if (brands != null && !brands.isEmpty()) {
                supportedCardTypes = new ArrayList<>();
                for (String brand : brands) {
                    final CardType brandType = CardType.getByBrandName(brand);
                    if (brandType != null) {
                        supportedCardTypes.add(brandType);
                    } else {
                        Logger.e(TAG, "Failed to get card type for brand: " + brand);
                    }
                }
            } else {
                Logger.d(TAG, "Falling back to DEFAULT_SUPPORTED_CARDS_LIST");
            }

            return cardConfiguration.newBuilder()
                    .setSupportedCardTypes(supportedCardTypes.toArray(new CardType[0]))
                    .build();
        }
        return cardConfiguration;
    }
}
