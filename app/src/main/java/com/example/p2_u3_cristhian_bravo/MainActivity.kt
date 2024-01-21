package com.example.p2_u3_cristhian_bravo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.p2_u3_cristhian_bravo.data.Solicitud
import com.example.p2_u3_cristhian_bravo.mock.FakeSolicitudDao
import com.example.p2_u3_cristhian_bravo.mock.FakeSolicitudListViewModel
import com.example.p2_u3_cristhian_bravo.ui.CameraScreen
import com.example.p2_u3_cristhian_bravo.ui.SolicitudListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyApp(this) }
    }
}

@Composable
fun MyApp(context: Context) {
    var currentScreen = remember { mutableStateOf("HomePage") }
    val viewModel = viewModel<SolicitudListViewModel>(factory = SolicitudListViewModel.Factory)

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                currentScreen.value = "Camera"

            } else {
            }
        }
    )

    val onRequestCameraPermission = {
        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    val onIniciarSesionConPermisoYUbicacion = {currentScreen.value = "SolicitudCuenta"
    }
    val onRegistrarseClicked = {
        currentScreen.value = "SolicitudCuenta"
    }

    when (currentScreen.value) {
        "HomePage" -> HomePageUI(
            viewModel = viewModel,
            onRegistrarseClicked = onRegistrarseClicked,
            onIniciarSesionConPermisoYUbicacion = onIniciarSesionConPermisoYUbicacion
        )
        "SolicitudCuenta" -> SolicitudCuentaUI(
            viewModel = viewModel,
            onRequestCameraPermission = onRequestCameraPermission,
            onNavigateToCamera = { currentScreen.value = "Camera" }
        )
        "Camera" -> CameraScreen(
            onImageCaptured = { imagePath ->
                currentScreen.value = "SolicitudCuenta"
            },
            onError = { exception ->
            }
        )
    }
}


@Composable
fun HomePageUI(
    viewModel: SolicitudListViewModel,
    onRegistrarseClicked: () -> Unit = {},
    onIniciarSesionConPermisoYUbicacion: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var permisoConcedido by remember { mutableStateOf(false) }
    var ubicacionObtenida by remember { mutableStateOf(false) }

    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        permisoConcedido = permisos[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
    }

    LaunchedEffect(key1 = true) {
        permisoLauncher.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.iplabank_logo),
                contentDescription = "Logo Banco"
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { newValue ->
                    username = newValue
                },
                label = { Text("Nombre de usuario") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { newValue ->
                    password = newValue
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(30.dp))

            var ubicacionObtenida by remember { mutableStateOf(false) }

            Button(
                onClick = {

                    if (permisoConcedido && ubicacionObtenida) {
                        onIniciarSesionConPermisoYUbicacion()
                    } else {
                        permisoLauncher.launch(arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Iniciar Sesión", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRegistrarseClicked,
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))

            ) {
                Text("Registrarse", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showSystemUi = true, locale = "es")
@Composable
fun HomePageUIPreview() {
    val fakeViewModel = FakeSolicitudListViewModel(FakeSolicitudDao(), null)
    HomePageUI(
        viewModel = fakeViewModel,
        onRegistrarseClicked = {},
        onIniciarSesionConPermisoYUbicacion = {}
    )
}


@Composable
fun SolicitudCuentaUI(
    viewModel: SolicitudListViewModel,
    onRequestCameraPermission: () -> Unit,
    onNavigateToCamera: () -> Unit
) {
    var nombreCompleto by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Solicitud Cuenta") },
        contentColor = Color(0xFF4CAF50),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = nombreCompleto,
                        onValueChange = { newValue -> nombreCompleto = newValue },
                        label = { Text("Nombre Completo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rut,
                        onValueChange = {newValue -> rut = newValue },
                        label = { Text("RUT") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = { newValue -> fechaNacimiento = newValue },
                        label = { Text("Fecha Nacimiento") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { newValue -> email = newValue },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { newValue -> telefono = newValue },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onRequestCameraPermission() },
                        modifier = Modifier
                            .width(150.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Cédula Frontal")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onRequestCameraPermission() },
                        modifier = Modifier
                            .width(150.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Cédula Trasera")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                                val nuevaSolicitud = Solicitud(
                                nombreCompleto = "Nombre Ejemplo",
                                rut = "RUT Ejemplo",
                                fechaNacimiento = "Fecha Nacimiento",
                                email = "Email",
                                telefono = "Teléfono",
                                latitud = 0.0,
                                longitud = 0.0,
                                imagenCedulaFrontal = "",
                                imagenCedulaTrasera = ""
                                )
                                viewModel.agregaSolicitud(nuevaSolicitud)

                        },
                        modifier = Modifier
                            .width(150.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("SOLICITAR")
                    }
                }
            }
        }
    )
}



@Preview(showSystemUi = true, locale = "es")
@Composable
fun SolicitudCuentaUIPreview() {
    val fakeDao = FakeSolicitudDao()
    val fakeViewModel = FakeSolicitudListViewModel(fakeDao, null)
    SolicitudCuentaUI(
        viewModel = fakeViewModel,
        onRequestCameraPermission = {   },
        onNavigateToCamera = {  }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF4CAF50)
        )
    )
}