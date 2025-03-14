package mx.alumnos.uacj.example.la_calculadora

import android.R.attr.strokeWidth
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.alumnos.uacj.example.la_calculadora.ui.theme.La_CalculadoraTheme

data class BotonModelo(
    val id: String,
    var numero: String,
    var operacion_aritmetica: OperacionesAritmeticas = OperacionesAritmeticas.Ninguna,
    var operacion_a_mostrar: String = ""
){}


enum class EstadosCalculadora{
    CuandoEstaEnCero,
    AgregandoNumeros,
    SeleccionandoOperacion,
    MostrarResultado
}

enum class OperacionesAritmeticas{
    Ninguna, //Esta es la opcion por default y sirve para hacer nada
    Suma,
    Resta,
    Multiplicacion,
    Division,
    Cuadrado,
    Resultado
}

var hileras_de_botones_a_dibujar = arrayOf(
    arrayOf(
        BotonModelo("boton_7", "7", OperacionesAritmeticas.Multiplicacion, "*"),
        BotonModelo("boton_8", "8", OperacionesAritmeticas.Cuadrado, "x²"),
        BotonModelo("boton_9", "9", OperacionesAritmeticas.Division, "/")
    ),
    arrayOf(
        BotonModelo("boton_4", "4"),
        BotonModelo("boton_5", "5", OperacionesAritmeticas.Resultado, "="),
        BotonModelo("boton_6", "6")
    ),
    arrayOf(
        BotonModelo("boton_1", "1", OperacionesAritmeticas.Resta, "-"),
        BotonModelo("boton_2", "2"),
        BotonModelo("boton_3", "3", OperacionesAritmeticas.Suma, "+")
    ),
    arrayOf(
        BotonModelo("boton_0", "0"),
        BotonModelo("boton_punto", "."),
        BotonModelo("boton_operacion", "OP")
    )

)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            La_CalculadoraTheme {
                Calculadora()
            }
        }
    }
}

@Composable
fun Calculadora() {
    var pantalla_calculadora = remember { mutableStateOf("0") }
    var numero_anterior = remember { mutableStateOf("0") }
    var estado_de_la_calculadora = remember { mutableStateOf(EstadosCalculadora.CuandoEstaEnCero) }
    var operacion_seleccionada = remember { mutableStateOf((OperacionesAritmeticas.Ninguna)) }

    fun pulsar_boton(boton: BotonModelo){
        Log.v("BOTONES-INTERFZ", "Se ha pulsado el boton ${boton.id} de la interfaz")
        Log.v("OPERACION_SELECIONADA", "La operacion selecionada es ${operacion_seleccionada.value}")

        when(estado_de_la_calculadora.value){
            EstadosCalculadora.CuandoEstaEnCero -> {
                if(boton.id == "boton_0"){
                    return
                }
                else if(boton.id == "boton-punto"){
                    pantalla_calculadora.value = pantalla_calculadora.value + boton.numero
                    return
                }
                pantalla_calculadora.value = boton.numero
                estado_de_la_calculadora.value = EstadosCalculadora.AgregandoNumeros
            }

            EstadosCalculadora.AgregandoNumeros -> {
                if(boton.id == "boton_operacion"){
                    estado_de_la_calculadora.value = EstadosCalculadora.SeleccionandoOperacion
                    return
                }
                pantalla_calculadora.value = pantalla_calculadora.value + boton.numero
            }

            EstadosCalculadora.SeleccionandoOperacion -> {
                if(boton.operacion_aritmetica != OperacionesAritmeticas.Ninguna  &&
                    boton.operacion_aritmetica != OperacionesAritmeticas.Resultado
                    ){
                        if(boton.id == "boton_8")
                        {
                            operacion_seleccionada.value = boton.operacion_aritmetica

                            pantalla_calculadora.value = (pantalla_calculadora.value.toInt() * pantalla_calculadora.value.toInt()).toString()
                            estado_de_la_calculadora.value = EstadosCalculadora.MostrarResultado
                        }
                        else{
                            operacion_seleccionada.value = boton.operacion_aritmetica
                            estado_de_la_calculadora.value = EstadosCalculadora.CuandoEstaEnCero

                            numero_anterior.value = pantalla_calculadora.value

                            pantalla_calculadora.value = "0"
                        }
                    return
                }// Aqui imprimimos el resultado

                else if(boton.operacion_aritmetica == OperacionesAritmeticas.Resultado &&
                    operacion_seleccionada.value != OperacionesAritmeticas.Ninguna) {


                    when(operacion_seleccionada.value){
                        OperacionesAritmeticas.Suma -> {
                            pantalla_calculadora.value = (numero_anterior.value.toInt() + pantalla_calculadora.value.toInt()).toString()
                        }
                        OperacionesAritmeticas.Resta ->{
                            pantalla_calculadora.value = (numero_anterior.value.toInt() - pantalla_calculadora.value.toInt()).toString()
                        }
                        OperacionesAritmeticas.Multiplicacion ->{
                            pantalla_calculadora.value = (numero_anterior.value.toInt() * pantalla_calculadora.value.toInt()).toString()
                        }
                        OperacionesAritmeticas.Division ->{
                            pantalla_calculadora.value = (numero_anterior.value.toInt() / pantalla_calculadora.value.toInt()).toString()
                        }
                        else ->{}
                    }


                    estado_de_la_calculadora.value = EstadosCalculadora.MostrarResultado
                    return
                }

                estado_de_la_calculadora.value = EstadosCalculadora.AgregandoNumeros
            }


            EstadosCalculadora.MostrarResultado ->{
                numero_anterior.value = ""
                pantalla_calculadora.value = "0"
                estado_de_la_calculadora.value = EstadosCalculadora.CuandoEstaEnCero
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${pantalla_calculadora.value}", modifier =  Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.33f)
            .background(Color.Gray)
            .padding(25.dp)
            .height(50.dp)
            .drawBehind{
                drawLine(
                    Color.LightGray,
                    Offset(-70f, 720f),
                    Offset(size.width+70, 720f),
                    25f
                )
            },
            textAlign = TextAlign.Right,
            color = Color.White,
            fontSize = 56.sp
        )

        Column (modifier = Modifier.fillMaxHeight().background(Color.DarkGray)) {
                for (hilera_de_botones in hileras_de_botones_a_dibujar) {
                    Row(horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth().height(150.dp)){
                        for(boton_a_dibujar in hilera_de_botones) {
                            when(estado_de_la_calculadora.value){
                                EstadosCalculadora.SeleccionandoOperacion -> {
                                    Boton(boton_a_dibujar.operacion_a_mostrar, alPulsar = {
                                        pulsar_boton(boton_a_dibujar)
                                    })
                                }

                                else ->{
                                    Boton(boton_a_dibujar.numero, alPulsar = {
                                        pulsar_boton(boton_a_dibujar)
                                    })
                                }
                            }

                        }
                    }
                }




        }
    }

}

@Composable
fun Boton(etiqueta: String, alPulsar: () -> Unit = {}){
    Button(onClick = alPulsar, modifier = Modifier.padding(10.dp).width(80.dp).height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray
        )) {
        /*Box{
            Image(
                painter = painterResource(R.drawable.makima),
                contentDescription = "Foto de mami Makima",
                modifier = Modifier.size(25.dp)
            )

        }*/
        Text(etiqueta, modifier = Modifier,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 20.sp,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    La_CalculadoraTheme {
        Calculadora()
    }
}

@Preview(showBackground = true)
@Composable
fun mostrar_boton(){
    La_CalculadoraTheme {
        Boton("4", alPulsar = {})
    }
}