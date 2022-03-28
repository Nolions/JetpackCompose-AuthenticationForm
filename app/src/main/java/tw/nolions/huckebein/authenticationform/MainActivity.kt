package tw.nolions.huckebein.authenticationform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.nolions.huckebein.authenticationform.screen.AuthenticationForm
import tw.nolions.huckebein.authenticationform.ui.theme.AuthenticationFormTheme
import tw.nolions.huckebein.authenticationform.viewModel.AuthenticationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthenticationFormTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Authentication()
                }
            }
        }
    }
}


@Composable
fun Authentication() {
    val viewModel: AuthenticationViewModel = viewModel()

    MaterialTheme {
        AuthenticationContent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState = viewModel.uiState.collectAsState().value,
            handleEvent = viewModel::handleEvent
        )
    }
}

@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    authenticationState: AuthenticationState,
    handleEvent: (event: AuthenticationEvent) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (authenticationState.isLoading) {
            CircularProgressIndicator()
        } else {

        }
    }

    AuthenticationForm(
        modifier = Modifier.fillMaxSize(),
        authenticationMode = authenticationState.authenticationMode,
        email = authenticationState.email,
        password = authenticationState.password,
        enableAuthentication = authenticationState.isFormValid(),
        completedPasswordRequirements = authenticationState.passwordRequirements,
        onEmailChanged = {
            handleEvent(AuthenticationEvent.EmailChanged(it))
        },
        onPasswordChanged = {
            handleEvent(AuthenticationEvent.PasswordChanged(it))
        },
        onAuthenticate = {
            handleEvent(AuthenticationEvent.Authenticate)
        },
        onToggleMode = {
            handleEvent(AuthenticationEvent.ToggleAuthenticationMode)
        }
    )

    authenticationState.error?.let { error ->
        AuthenticationErrorDialog(
            error = error,
            dismissError = {
                handleEvent(
                    AuthenticationEvent.ErrorDismissed)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AuthenticationFormTheme {
        Authentication()
    }
}