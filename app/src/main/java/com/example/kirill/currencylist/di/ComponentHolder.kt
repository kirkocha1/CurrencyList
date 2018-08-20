package com.example.kirill.currencylist.di

object ComponentHolder {

    private var currencyListComponent: CurrencyListComponent? = null

    fun isExist() = currencyListComponent != null

    fun createComponent(): CurrencyListComponent {
        currencyListComponent = DaggerCurrencyListComponent.create()
        return currencyListComponent!!
    }

    fun getCurrencyListComponent() = currencyListComponent
            ?: throw ComponentNotCreatedException("${CurrencyListComponent::class.java.simpleName} wasn't created")

    fun destroyComponent() {
        currencyListComponent = null
    }
}

class ComponentNotCreatedException(message: String) : Exception(message)
