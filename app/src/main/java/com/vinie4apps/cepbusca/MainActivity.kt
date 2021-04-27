package com.vinie4apps.cepbusca

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


private var requestQueue: RequestQueue? = null
@SuppressLint("StaticFieldLeak")
lateinit var textView: TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        val editText = findViewById<View>(R.id.campo) as EditText
        val str = editText.text

        val bnt = findViewById<Button>(R.id.button_parse)
        textView = findViewById<View>(R.id.text_view_result) as TextView


        bnt.setOnClickListener {
            jsonParse(str)

        }

    }

    private fun jsonParse(str: Editable) {
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

                textView.append("Logradouro: $log\nBairro: $bairro\nComplemento: $complemento\nLocal: $local\nUF: $uf\nIBGE: $ibge\nDDD: $ddd\nSIAFI: $siafi")


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }
}
