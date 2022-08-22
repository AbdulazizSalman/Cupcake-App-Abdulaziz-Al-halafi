package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.5
private const val PRICE_FOR_SAME_DAY = 2.5
class OrderViewModelL:ViewModel() {

    val dateOptions = getPickupOptions()

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> get() = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> get() = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> get() = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }



    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int){
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor:String){
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate:String){
        _date.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavor():Boolean{
        return _flavor.value.isNullOrBlank()
    }
    fun hasNoDate():Boolean{
        return _date.value.isNullOrBlank()
    }

    private fun getPickupOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter =  SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE,1)
        }
        return options
    }

    fun resetOrder(){
        _quantity.value = 0
        _flavor.value =""
        _date.value=""
        _price.value = 0.0
    }
    private fun updatePrice(){
        var calcPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if(_date.value==dateOptions[0]){
            calcPrice+= PRICE_FOR_SAME_DAY
        }
        _price.value = calcPrice
    }

}