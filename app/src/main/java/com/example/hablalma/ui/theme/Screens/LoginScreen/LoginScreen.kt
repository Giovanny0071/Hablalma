package com.example.hablalma.ui.theme.Login

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.hablalma.ui.theme.Screens.LoginScreen.LoginViewModel

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
    navController: NavHostController,
    loginViewModel: LoginViewModel = LoginViewModel()
) {
    val context = LocalContext.current
    val isLoading by loginViewModel.isLoading.collectAsStateWithLifecycle()
    val loginSuccess by loginViewModel.loginSuccess.collectAsStateWithLifecycle()
    val errorMessage by loginViewModel.errorMessage.collectAsStateWithLifecycle()
    val googleToastMessage by loginViewModel.googleToastMessage.collectAsStateWithLifecycle()

    var enRegistro by rememberSaveable { mutableStateOf(false) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate("inicio") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(googleToastMessage) {
        googleToastMessage?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            loginViewModel.consumeGoogleToastMessage()
        }
    }

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
                    SeccionNombre(nombre = nombre, onNombreChange = { nombre = it })
                }

                SeccionCorreo(correo = correo, onCorreoChange = { correo = it })

                SeccionContrasena(
                    contrasena = contrasena,
                    onContrasenaChange = { contrasena = it }
                )
            }

            if (!enRegistro) {
                TextButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        disabledContentColor = TextoSecundario
                    )
                ) {
                    Text("¿Has olvidado tu contraseña?")
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (enRegistro) {
                        loginViewModel.registerWithEmail(correo.trim(), contrasena)
                    } else {
                        loginViewModel.loginWithEmail(correo.trim(), contrasena)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                shape = FormaCampo,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoOscuro,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (enRegistro) "Crear cuenta" else "Iniciar sesión",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(16.dp))

            SeparadorGoogle()

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { loginViewModel.launchGoogle(context, enRegistro) },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                shape = FormaCampo,
                border = BorderStroke(1.dp, BordeCampo),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MoradoOscuro
                )
            ) {
                Text(
                    text = if (enRegistro) "Registrarme con Google" else "Entrar con Google",
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            if (isLoading) {
                Spacer(Modifier.height(16.dp))

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally).size(24.dp),
                    color = MoradoOscuro,
                    strokeWidth = 2.dp
                )
            }

            errorMessage?.let { mensaje ->
                Spacer(Modifier.height(12.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.error)
            }
        }

        TextButton(
            onClick = {
                enRegistro = !enRegistro
                contrasena = ""
            },
            enabled = !isLoading,
            modifier = Modifier.padding(top = 12.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = MoradoOscuro)
        ) {
            Text(
                text = if (enRegistro) {
                    "¿Ya tienes cuenta? Inicia sesión"
                } else {
                    "¿No tienes cuenta? Regístrate"
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CabeceraHablalma() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "H",
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF542682), MoradoOscuro)
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 22.dp, vertical = 10.dp),
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Hablalma",
            color = MoradoOscuro,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Un espacio para hablar y escuchar",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun SeccionNombre(nombre: String, onNombreChange: (String) -> Unit) {
    CampoFormulario(
        valor = nombre,
        etiqueta = "Nombre",
        onValueChange = onNombreChange
    )
}

@Composable
private fun SeccionCorreo(correo: String, onCorreoChange: (String) -> Unit) {
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
    onContrasenaChange: (String) -> Unit
) {
    CampoFormulario(
        valor = contrasena,
        etiqueta = "Contraseña",
        onValueChange = onContrasenaChange,
        tipoTeclado = KeyboardType.Password,
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
private fun SeparadorGoogle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = BordeSuave)
        Text("o", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        HorizontalDivider(modifier = Modifier.weight(1f), color = BordeSuave)
    }
}