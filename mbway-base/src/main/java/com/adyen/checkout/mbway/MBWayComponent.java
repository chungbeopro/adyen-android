/*
 * Copyright (c) 2020 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by caiof on 11/8/2020.
 */

package com.adyen.checkout.mbway;

import androidx.annotation.NonNull;

import com.adyen.checkout.base.PaymentComponentProvider;
import com.adyen.checkout.base.PaymentComponentState;
import com.adyen.checkout.base.component.BasePaymentComponent;
import com.adyen.checkout.base.component.GenericPaymentComponentProvider;
import com.adyen.checkout.base.component.GenericPaymentMethodDelegate;
import com.adyen.checkout.base.model.payments.request.MBWayPaymentMethod;
import com.adyen.checkout.base.model.payments.request.PaymentComponentData;
import com.adyen.checkout.base.util.PaymentMethodTypes;
import com.adyen.checkout.core.log.LogUtil;
import com.adyen.checkout.core.log.Logger;

@SuppressWarnings("AbbreviationAsWordInName")
public class MBWayComponent
        extends BasePaymentComponent<MBWayConfiguration, MBWayInputData, MBWayOutputData, PaymentComponentState<MBWayPaymentMethod>> {
    private static final String TAG = LogUtil.getTag();

    public static final PaymentComponentProvider<MBWayComponent, MBWayConfiguration> PROVIDER =
            new GenericPaymentComponentProvider<>(MBWayComponent.class);

    private static final String[] PAYMENT_METHOD_TYPES = {PaymentMethodTypes.MB_WAY};

    /**
     * Component should not be instantiated directly. Instead use the PROVIDER object.
     *
     * @param paymentMethodDelegate {@link GenericPaymentMethodDelegate}
     * @param configuration {@link MBWayConfiguration}
     */
    public MBWayComponent(@NonNull GenericPaymentMethodDelegate paymentMethodDelegate, @NonNull MBWayConfiguration configuration) {
        super(paymentMethodDelegate, configuration);
    }

    @NonNull
    @Override
    protected MBWayOutputData onInputDataChanged(@NonNull MBWayInputData inputData) {
        Logger.v(TAG, "onInputDataChanged");
        return new MBWayOutputData(inputData.getEmail(), inputData.getMobilePhoneNumber());
    }

    @NonNull
    @Override
    protected PaymentComponentState createComponentState() {
        final MBWayOutputData mbWayOutputData = getOutputData();

        final PaymentComponentData<MBWayPaymentMethod> paymentComponentData = new PaymentComponentData<>();
        final MBWayPaymentMethod paymentMethod = new MBWayPaymentMethod();
        paymentMethod.setType(MBWayPaymentMethod.PAYMENT_METHOD_TYPE);

        if (mbWayOutputData != null) {
            paymentMethod.setTelephoneNumber(mbWayOutputData.getMobilePhoneNumberField().getValue());
            paymentMethod.setShopperEmail(mbWayOutputData.getEmailField().getValue());
        }

        paymentComponentData.setPaymentMethod(paymentMethod);

        return new PaymentComponentState<>(paymentComponentData, mbWayOutputData != null && mbWayOutputData.isValid());
    }

    @NonNull
    @Override
    public String[] getSupportedPaymentMethodTypes() {
        return PAYMENT_METHOD_TYPES;
    }
}
