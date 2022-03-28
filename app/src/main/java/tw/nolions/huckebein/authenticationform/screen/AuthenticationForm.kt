package tw.nolions.huckebein.authenticationform.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tw.nolions.huckebein.authenticationform.*
import tw.nolions.huckebein.authenticationform.R

@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    email: String?,
    password: String?,
    completedPasswordRequirements: List<PasswordRequirements>,
    enableAuthentication: Boolean,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    onToggleMode: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationTitle(authenticationMode = authenticationMode)

        Spacer(modifier = Modifier.height(40.dp))
        val passwordFocusRequester = FocusRequester()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailInput(
                    modifier = Modifier.fillMaxWidth(),
                    email = email ?: "",
                    onEmailChanged = onEmailChanged
                ) {
                    passwordFocusRequester.requestFocus()
                }

                Spacer(modifier = Modifier.height(16.dp))
                PasswordInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester),
                    password = password ?: "",
                    onPasswordChanged = onPasswordChanged,
                    onDoneClicked = onAuthenticate
                )

                Spacer(modifier = Modifier.height(12.dp))
                AnimatedVisibility(visible = authenticationMode == AuthenticationMode.SIGN_UP) {
                    PasswordRequirements(satisfiedRequirements = completedPasswordRequirements)
                }

                Spacer(modifier = Modifier.height(12.dp))

                AuthenticationButton(
                    enableAuthentication = enableAuthentication,
                    authenticationMode = authenticationMode,
                    onAuthenticate = onAuthenticate
                )

                Spacer(modifier = Modifier.weight(1f))

                ToggleAuthenticationMode(
                    modifier = Modifier.fillMaxWidth(),
                    authenticationMode = authenticationMode,
                    toggleAuthentication = {
                        onToggleMode()
                    }
                )
            }
        }
    }
}

@Composable
fun Requirement(
    modifier: Modifier = Modifier,
    message: String,
    satisfied: Boolean
) {
    val requirementStatus = if (satisfied) {
        stringResource(id = R.string.password_requirement_satisfied, message)
    } else {
        stringResource(id = R.string.password_requirement_needed, message)
    }

    val tint = if (satisfied) {
        MaterialTheme.colors.onSurface
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
    }

    Row(
        modifier = modifier
            .padding(6.dp)
            .semantics(mergeDescendants = true) {
                text = AnnotatedString(requirementStatus)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.clearAndSetSemantics { },
            text = message,
            fontSize = 12.sp,
            color = tint
        )
    }
}

@Composable
fun PasswordRequirements(
    modifier: Modifier = Modifier,
    satisfiedRequirements: List<PasswordRequirements>
) {
    Column(
        modifier = modifier
    ) {
        PasswordRequirements.values().forEach { requirement ->
            Requirement(
                message = stringResource(
                    id = requirement.label
                ),
                satisfied = satisfiedRequirements.contains(
                    requirement
                )
            )
        }
    }
}

@Composable
fun ToggleAuthenticationMode(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    toggleAuthentication: () -> Unit
) {
    Surface(
        modifier = modifier.padding(top = 16.dp),
        elevation = 8.dp
    ) {
        TextButton(
            onClick = {
                toggleAuthentication()
            }
        ) {
            Text(
                text = stringResource(
                    if (authenticationMode == AuthenticationMode.SIGN_IN) {
                        R.string.action_need_account
                    } else {
                        R.string.action_already_have_account
                    }
                )
            )
        }
    }
}