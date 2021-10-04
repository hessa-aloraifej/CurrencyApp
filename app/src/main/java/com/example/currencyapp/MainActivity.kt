package com.example.currencyapp

import android.os.Bundle


import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var curencyDetails: CurrencyDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userinput = findViewById<EditText>(R.id.userinput)
        val convrt = findViewById<Button>(R.id.btn)
        val spinner = findViewById<Spinner>(R.id.spr)

        val cur = arrayListOf("Select Any Currency","inr", "usd", "aud", "sar", "cny", "jpy")

        var selected: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter



            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selected = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
        convrt.setOnClickListener {

            var sel = userinput.text.toString().toDouble()


            getCurrency(onResult = {
                curencyDetails = it

                when (selected) {
                    1 -> disp(calc(curencyDetails?.eur?.inr?.toDouble(), sel));
                    2 -> disp(calc(curencyDetails?.eur?.usd?.toDouble(), sel));
                    3 -> disp(calc(curencyDetails?.eur?.aud?.toDouble(), sel));
                    4 -> disp(calc(curencyDetails?.eur?.sar?.toDouble(), sel));
                    5 -> disp(calc(curencyDetails?.eur?.cny?.toDouble(), sel));
                    6 -> disp(calc(curencyDetails?.eur?.jpy?.toDouble(), sel));
                }
            })
        }

    }

    private fun disp(calc: Double) {

        val responseText = findViewById<View>(R.id.textView3) as TextView

        responseText.text = "Result " + calc
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = (i * sel)
        }
        return s
    }

    private fun getCurrency(onResult: (CurrencyDetails?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<CurrencyDetails> {
                override fun onResponse(
                    call: Call<CurrencyDetails>,
                    response: Response<CurrencyDetails>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<CurrencyDetails>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}