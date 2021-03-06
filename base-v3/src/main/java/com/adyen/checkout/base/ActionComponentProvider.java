/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by caiof on 13/5/2019.
 */

package com.adyen.checkout.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelStoreOwner;

import com.adyen.checkout.base.component.Configuration;

public interface ActionComponentProvider<ComponentT extends ActionComponent> extends ComponentProvider<ComponentT> {

    /**
     * Get an {@link ActionComponent}.
     *
     * @param viewModelStoreOwner The Activity or Fragment to associate the lifecycle.
     * @param configuration The Configuration of the component. Can be null in most cases.
     * @return The Component
     */
    @SuppressWarnings("LambdaLast")
    @NonNull
    ComponentT get(@NonNull ViewModelStoreOwner viewModelStoreOwner, @NonNull Application application, @Nullable Configuration configuration);

    /**
     * @return If the Configuration is required for this Component.
     */
    boolean requiresConfiguration();
}
