package com.vinie4apps.cepbusca

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.json.JSONException


private lateinit var requestQueue: RequestQueue
@SuppressLint("StaticFieldLeak")
private lateinit var textView: TextView
private lateinit var textUF: String
lateinit var mAdView : AdView

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        requestQueue = Volley.newRequestQueue(this)

        val cidade = findViewById<View>(R.id.cidade) as EditText
        val strcidade = cidade.text

        val end = findViewById<View>(R.id.end) as EditText
        val strend = end.text

        val editText = findViewById<View>(R.id.campo) as EditText
        val str = editText.text

        val card = findViewById<CardView>(R.id.cardv)

        val bntcep = findViewById<Button>(R.id.bntcep)
        val bntend = findViewById<Button>(R.id.bntend)

        textView = findViewById<View>(R.id.text_view_result) as TextView


        bntcep.setOnClickListener {
            card.visibility = View.VISIBLE
            jsonParseCEP(str)

        }
        bntend.setOnClickListener {
            card.visibility = View.VISIBLE
            jsonParseEND(strcidade, strend)

        }

        val spinner = findViewById<Spinner>(R.id.uf)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.UF, R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        textUF = parent.getItemAtPosition(position).toString()
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun jsonParseCEP(str: Editable) {
        textView.text = " "
        val url = "https://viacep.com.br/ws/$str/json/"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {

                val log = response.getString("logradouro")
                val complemento = response.getString("complemento")
                val bairro = response.getString("bairro")
                val local = response.getString("localidade")
                val uf = response.getString("uf")
                val ibge = response.getString("ibge")
                val ddd = response.getString("ddd")
                val siafi = response.getString("siafi")

                textView.append("Logradouro: $log\n Bairro: $bairro\n Complemento: $complemento\n Local: $local\n UF: $uf\n IBGE: $ibge\n DDD: $ddd\n SIAFI: $siafi")


            } catch (e: JSONException) {
                e.printStackTrace()
                textView.append("CEP não encontrado :(")
            }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
    private fun jsonParseEND(cidade: Editable, end: Editable) {
        textView.text = " "
        val url = "https://viacep.com.br/ws/$textUF/$cidade/$end/json/"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            try {

                val cep = response.getJSONObject(0).getString("cep")
                val bairro = response.getJSONObject(0).getString("bairro")
                val ibge = response.getJSONObject(0).getString("ibge")
                val ddd = response.getJSONObject(0).getString("ddd")
                val siafi = response.getJSONObject(0).getString("siafi")
                textView.append("CEP: $cep\n Bairro: $bairro\n IBGE: $ibge\n DDD: $ddd\n SIAFI: $siafi")


            } catch (e: JSONException) {
                e.printStackTrace()
                textView.append("CEP não encontrado :(")
            }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
}