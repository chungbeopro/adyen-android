/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by ran on 19/3/2019.
 */

package com.adyen.checkout.card.data.formatter;

import android.support.annotation.NonNull;

public interface SecurityCodeFormatter {
    /**
     * Formats a security code.
     *
     * @param securityCode The security code to be formatted.
     * @return The formatted security code.
     */
    @NonNull
    String formatSecurityCode(@NonNull String securityCode);
}