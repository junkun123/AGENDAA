package com.example.agenda11

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda11.databinding.ActivityCalculadoraBinding
import java.text.DecimalFormat
import kotlin.math.abs

class CalculadoraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculadoraBinding
    private lateinit var textViewResultado: TextView

    private var operandoActual: Double = 0.0
    private var operandoAnterior: Double = 0.0
    private var operacionPendiente: String? = null
    private var entradaUsuario: StringBuilder = StringBuilder("0")
    private var reinicioEntradaNecesario: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculadoraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textViewResultado = binding.textViewResultadoCalculadora
        actualizarPantalla()

        setNumericOnClickListener()
        setOperatorOnClickListener()
    }

    private fun formatNumberForDisplay(number: Double): String {
        if (number.isInfinite() || number.isNaN()) {
            reinicioEntradaNecesario = true
            return "Error"
        }

        val absNumber = abs(number)
        return if ((absNumber >= 1E10 || (absNumber < 1E-6 && absNumber != 0.0))) {
            val scientificFormat = DecimalFormat("0.######E0")
            scientificFormat.format(number)
        } else {
            val normalFormat = DecimalFormat("0.##########")
            normalFormat.format(number)
        }
    }

    private fun actualizarPantalla() {
        textViewResultado.text = if (entradaUsuario.toString() == "-") "0" else entradaUsuario.toString()
    }

    private fun setNumericOnClickListener() {
        val listener = View.OnClickListener { v ->
            val button = v as Button
            val digito = button.text.toString()

            if (reinicioEntradaNecesario) {
                entradaUsuario.clear()
                entradaUsuario.append("0")
                reinicioEntradaNecesario = false
            }

            // Permitir solo un punto decimal
            if (digito == "." && entradaUsuario.contains(".")) {
                return@OnClickListener
            }

            // Si la entrada actual es "0" y se presiona un dígito (no "."), reemplazar el "0"
            if (entradaUsuario.toString() == "0" && digito != ".") {
                entradaUsuario.clear()
            } else if (entradaUsuario.toString() == "-0" && digito != ".") { // Para manejar "-0" y luego un dígito -> "-5"
                entradaUsuario.deleteCharAt(entradaUsuario.length - 1) // Quitar el '0' de "-0"
            }

            // Evitar múltiples ceros al inicio a menos que sea "0."
            if (entradaUsuario.toString() == "0" && digito == "0") {
                // No hacer nada, ya es "0"
            } else if (entradaUsuario.toString() == "-0" && digito == "0") {
                 // No hacer nada, ya es "-0"
            } else {
                entradaUsuario.append(digito)
            }
            actualizarPantalla()
        }

        binding.button0.setOnClickListener(listener)
        binding.button1.setOnClickListener(listener)
        binding.button2.setOnClickListener(listener)
        binding.button3.setOnClickListener(listener)
        binding.button4.setOnClickListener(listener)
        binding.button5.setOnClickListener(listener)
        binding.button6.setOnClickListener(listener)
        binding.button7.setOnClickListener(listener)
        binding.button8.setOnClickListener(listener)
        binding.button9.setOnClickListener(listener)
        binding.buttonPunto.setOnClickListener(listener)
    }

    private fun setOperatorOnClickListener() {
        binding.buttonLimpiar.setOnClickListener {
            entradaUsuario.clear()
            entradaUsuario.append("0")
            operandoAnterior = 0.0
            operandoActual = 0.0
            operacionPendiente = null
            reinicioEntradaNecesario = false
            actualizarPantalla()
        }

        binding.buttonBorrar.setOnClickListener {
            if (entradaUsuario.isNotEmpty() && entradaUsuario.toString() != "Error") {
                entradaUsuario.deleteCharAt(entradaUsuario.length - 1)
                if (entradaUsuario.isEmpty() || entradaUsuario.toString() == "-") {
                    entradaUsuario.append("0") 
                }
            } else if (entradaUsuario.toString() == "Error"){
                entradaUsuario.clear()
                entradaUsuario.append("0")
                reinicioEntradaNecesario = false
            }
            actualizarPantalla()
        }

        val opListener = View.OnClickListener { v ->
            val button = v as Button
            val operacion = button.text.toString()

            if (entradaUsuario.toString() == "Error") {
                 reinicioEntradaNecesario = true;
            } else if (entradaUsuario.isNotEmpty()) {
                try {
                    operandoActual = entradaUsuario.toString().toDouble()
                } catch (e: NumberFormatException) {
                    entradaUsuario.clear()
                    entradaUsuario.append(formatNumberForDisplay(Double.NaN))
                    actualizarPantalla()
                    reinicioEntradaNecesario = true
                    return@OnClickListener
                }
            }

            if (operacion == "-" && (entradaUsuario.toString() == "0" && operacionPendiente == null)) {
                entradaUsuario.clear()
                entradaUsuario.append("-")
                reinicioEntradaNecesario = false 
                actualizarPantalla()
                return@OnClickListener
            }

            if (operacionPendiente != null && !reinicioEntradaNecesario || (operacionPendiente != null && reinicioEntradaNecesario && entradaUsuario.toString() != "0" && entradaUsuario.toString() != "-")) {
                 if (entradaUsuario.toString() != "Error"){ 
                    calcular()
                 }
            }

            operandoAnterior = if (entradaUsuario.toString() == "Error") operandoAnterior else operandoActual
            operacionPendiente = operacion
            reinicioEntradaNecesario = true 

            if (entradaUsuario.toString() == "-") {
                // No action needed here beyond what's done
            }
        }

        binding.buttonSumar.setOnClickListener(opListener)
        binding.buttonRestar.setOnClickListener(opListener)
        binding.buttonMultiplicar.setOnClickListener(opListener)
        binding.buttonDividir.setOnClickListener(opListener)

        binding.buttonIgual.setOnClickListener {
            if (entradaUsuario.toString() == "Error"){
                 return@setOnClickListener
            }
            if (entradaUsuario.isNotEmpty() && operacionPendiente != null) {
                try {
                    operandoActual = entradaUsuario.toString().toDouble()
                } catch (e: NumberFormatException) {
                    entradaUsuario.clear()
                    entradaUsuario.append(formatNumberForDisplay(Double.NaN))
                    actualizarPantalla()
                    reinicioEntradaNecesario = true
                    return@setOnClickListener
                }
                calcular()
                operacionPendiente = null
                reinicioEntradaNecesario = true // Línea añadida
            }
        }
    }

    private fun calcular() {
        if (operacionPendiente == null) return

        var resultado = 0.0
        when (operacionPendiente) {
            "+" -> resultado = operandoAnterior + operandoActual
            "-" -> resultado = operandoAnterior - operandoActual
            "*" -> resultado = operandoAnterior * operandoActual
            "/" -> {
                if (operandoActual == 0.0) {
                    entradaUsuario.clear()
                    entradaUsuario.append(formatNumberForDisplay(Double.NaN))
                    actualizarPantalla()
                    operacionPendiente = null
                    return
                }
                resultado = operandoAnterior / operandoActual
            }
        }
        entradaUsuario.clear()
        entradaUsuario.append(formatNumberForDisplay(resultado))
        actualizarPantalla()
        operandoActual = resultado 
        // reinicioEntradaNecesario is managed by formatNumberForDisplay or operator listeners
    }
}
