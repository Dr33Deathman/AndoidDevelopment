package kz.kbtu.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import java.lang.Float.parseFloat
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var topNumberPanel : TextView
    private lateinit var bottomNumberPanel : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topNumberPanel = findViewById(R.id.top_number_panel)
        topNumberPanel.text = "0"
        bottomNumberPanel = findViewById(R.id.bottom_number_panel)
        bottomNumberPanel.text = "= 0"

        findViewById<TextView>(R.id._0).setOnClickListener { _ -> onNumberPress("0") }
        findViewById<TextView>(R.id._1).setOnClickListener { _ -> onNumberPress("1") }
        findViewById<TextView>(R.id._2).setOnClickListener { _ -> onNumberPress("2") }
        findViewById<TextView>(R.id._3).setOnClickListener { _ -> onNumberPress("3") }
        findViewById<TextView>(R.id._4).setOnClickListener { _ -> onNumberPress("4") }
        findViewById<TextView>(R.id._5).setOnClickListener { _ -> onNumberPress("5") }
        findViewById<TextView>(R.id._6).setOnClickListener { _ -> onNumberPress("6") }
        findViewById<TextView>(R.id._7).setOnClickListener { _ -> onNumberPress("7") }
        findViewById<TextView>(R.id._8).setOnClickListener { _ -> onNumberPress("8") }
        findViewById<TextView>(R.id._9).setOnClickListener { _ -> onNumberPress("9") }
        findViewById<TextView>(R.id._dot).setOnClickListener{ _ -> onDotPress() }

        findViewById<TextView>(R.id._plus).setOnClickListener { _ -> onOperationPress(" + ") }
        findViewById<TextView>(R.id._minus).setOnClickListener { _ -> onOperationPress(" - ") }
        findViewById<TextView>(R.id._multiply).setOnClickListener { _ -> onOperationPress(" * ") }
        findViewById<TextView>(R.id._divide).setOnClickListener { _ -> onOperationPress(" / ") }

        findViewById<TextView>(R.id._equals).setOnClickListener { _ -> onEqualsPress() }
        findViewById<TextView>(R.id._clear).setOnClickListener { _ -> onClear() }
        findViewById<TextView>(R.id._left_arrow).setOnClickListener { _ -> onLeftArrow() }

    }

    private fun onNumberPress(input: String) {
        if (topNumberPanel.text == "0") topNumberPanel.text = topNumberPanel.text.drop(1)
        topNumberPanel.text = topNumberPanel.text.toString().plus(input)
        updateBottomNumberPanel()
    }

    private fun onOperationPress(input: String) {
        if (topNumberPanel.text.last() == ' ') return
        topNumberPanel.text = topNumberPanel.text.toString().plus(input)
        updateBottomNumberPanel()
    }

    private fun onEqualsPress() {
        topNumberPanel.text = calculate().toString()
        updateBottomNumberPanel()
    }

    private fun onClear() {
        topNumberPanel.text = topNumberPanel.text.trim().dropLast(1).trim()
        if (topNumberPanel.text.isEmpty() || (topNumberPanel.text.first() == 'I' || topNumberPanel.text.first() == 'N')) topNumberPanel.text = "0"
        updateBottomNumberPanel()
    }

    private fun onDotPress() {
        if (topNumberPanel.text.last() == '.') return
        if (Regex(""".*\d+\.\d*$""").matches(topNumberPanel.text)) return
        topNumberPanel.text = topNumberPanel.text.toString().plus(".")
    }

    private fun onLeftArrow() {
        topNumberPanel.text = "0"
        updateBottomNumberPanel()
    }

    private fun calculate (): kotlin.Float {

        val expression = topNumberPanel.text.toString()
        val operation = Regex(""" [+*-/] """).find(expression)?.value?.trim()

        val numbers = Regex(""" [+-/*] """).split(expression)

        val firstOperand = parseFloat(numbers.first())
        val secondOperand = parseFloat(if (numbers.last() == "") "0" else numbers.last())

        if (numbers.size == 1) return firstOperand

        return when (operation) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "*" -> firstOperand * secondOperand
            "/" -> firstOperand / secondOperand
            else -> firstOperand
        }
    }

    private fun updateBottomNumberPanel () {
        topNumberPanel.textSize = max(10.0F, 75 - 2.0F * topNumberPanel.text.length.toFloat())
        bottomNumberPanel.text = "= ".plus(calculate().toString())
    }

}