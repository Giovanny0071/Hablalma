package com.example.hablalma.ui.theme.Screens.LoginScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoOscuro = Color(0xFF3D1766)
private val Lavanda = Color(0xFFF7F1FC)
private val TextoOscuro = Color(0xFF241630)
private val TextoSecundario = Color(0xFF665675)
private val BordeSuave = Color(0xFFE8DCEF)
private val BordeCampo = Color(0xFFB6A3C8)
private val FormaCampo = RoundedCornerShape(16.dp)
private val FormaTarjeta = RoundedCornerShape(28.dp)

@Composable
fun LoginScreen(
    onIniciarSesion: (correo: String, contrasena: String) -> Unit = { _, _ -> },
    onCrearCuenta: (nombre: String, correo: String, contrasena: String) -> Unit =
        { _, _, _ -> },
    onRecuperarContrasena: (correo: String) -> Unit = {},
    onGoogleClick: (enRegistro: Boolean) -> Unit = {}
) {
    var enRegistro by rememberSaveable { mutableStateOf(false) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFECE0FA), Lavanda, Color.White)
                )
            )
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CabeceraHablalma()

        Spacer(Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .background(Color.White, FormaTarjeta)
                .border(1.dp, BordeSuave, FormaTarjeta)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = if (enRegistro) "Crea tu cuenta" else "Te damos la bienvenida",
                color = TextoOscuro,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (enRegistro) {
                    SeccionNombre(
                        nombre = nombre,
                        onNombreChange = { nombre = it }
                    )
                }

                SeccionCorreo(
                    correo = correo,
                    onCorreoChange = { correo = it }
                )

                SeccionContrasena(
                    contrasena = contrasena,
                    onContrasenaChange = { contrasena = it },
                    enRegistro = enRegistro
                )
            }

            if (!enRegistro) {
                SeccionRecuperarContrasena(
                    onClick = { onRecuperarContrasena(correo.trim()) },
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(Modifier.height(16.dp))

            BotonPrincipal(
                texto = if (enRegistro) "Crear cuenta" else "Iniciar sesión",
                onClick = {
                    if (enRegistro) {
                        onCrearCuenta(nombre.trim(), correo.trim(), contrasena)
                    } else {
                        onIniciarSesion(correo.trim(), contrasena)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            SeccionGoogle(
                enRegistro = enRegistro,
                onClick = { onGoogleClick(enRegistro) }
            )
        }

        SeccionCambiarFormulario(
            enRegistro = enRegistro,
            onClick = {
                enRegistro = !enRegistro
                contrasena = ""
            }
        )
    }
}

@Composable
private fun CabeceraHablalma() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Hablalma",
            color = MoradoOscuro,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Un espacio para escuchar y ser escuchado",
            modifier = Modifier.padding(top = 4.dp),
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SeccionNombre(
    nombre: String,
    onNombreChange: (String) -> Unit
) {
    CampoFormulario(
        valor = nombre,
        etiqueta = "Nombre",
        onValueChange = onNombreChange
    )
}

@Composable
private fun SeccionCorreo(
    correo: String,
    onCorreoChange: (String) -> Unit
) {
    CampoFormulario(
        valor = correo,
        etiqueta = "Correo electrónico",
        onValueChange = onCorreoChange,
        tipoTeclado = KeyboardType.Email
    )
}

@Composable
private fun SeccionContrasena(
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    enRegistro: Boolean
) {
    CampoFormulario(
        valor = contrasena,
        etiqueta = "Contraseña",
        onValueChange = onContrasenaChange,
        tipoTeclado = if (enRegistro) KeyboardType.Password else KeyboardType.Password,
        transformacion = PasswordVisualTransformation()
    )
}

@Composable
private fun CampoFormulario(
    valor: String,
    etiqueta: String,
    onValueChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    transformacion: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        visualTransformation = transformacion,
        singleLine = true,
        shape = FormaCampo,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MoradoOscuro,
            unfocusedBorderColor = BordeCampo,
            focusedLabelColor = MoradoOscuro,
            unfocusedLabelColor = TextoSecundario,
            focusedTextColor = TextoOscuro,
            unfocusedTextColor = TextoOscuro,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = MoradoOscuro
        )
    )
}

@Composable
private fun SeccionRecuperarContrasena(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = "¿Has olvidado tu contraseña?",
            color = MoradoOscuro,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
        shape = FormaCampo,
        colors = ButtonDefaults.buttonColors(
            containerColor = MoradoOscuro,
            contentColor = Color.White
        )
    ) {
        Text(text = texto, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SeccionGoogle(
    enRegistro: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = BordeSuave)
        Text(
            text = "o",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = BordeSuave)
    }

    Spacer(Modifier.height(12.dp))

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
        shape = FormaCampo,
        border = BorderStroke(1.dp, BordeCampo),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = MoradoOscuro
        )
    ) {
        Text(
            text = if (enRegistro) "Registrarme con Google" else "Entrar con Google",
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SeccionCambiarFormulario(
    enRegistro: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(top = 12.dp)
    ) {
        Text(
            text = if (enRegistro) {
                "¿Ya tienes cuenta? Inicia sesión"
            } else {
                "¿No tienes cuenta? Regístrate"
            },
            color = MoradoOscuro,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 820)
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}