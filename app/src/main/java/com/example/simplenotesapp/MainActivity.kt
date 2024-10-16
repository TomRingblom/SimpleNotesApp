package com.example.simplenotesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simplenotesapp.navigation.Screen
import com.example.simplenotesapp.ui.AppViewModelProvider
import com.example.simplenotesapp.ui.home.HomeScreen
import com.example.simplenotesapp.ui.navigation.NavigationDestination
import com.example.simplenotesapp.ui.note.NoteAddScreen
import com.example.simplenotesapp.ui.note.NoteEditViewModel
import com.example.simplenotesapp.ui.theme.NoteEditDestination
import com.example.simplenotesapp.ui.theme.NoteEditScreen
import com.example.simplenotesapp.ui.theme.SimpleNotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNotesAppTheme {
                SimpleNotesApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleNotesAppBar(
//    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
//        title = { currentScreen.name },
        title = {"Hello" },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    )
}

//fun getScreenFromRoute(route: String?): NavigationDestination {
//    return when {
//        route == null -> Screen.Home
//        route.startsWith(NoteEditDestination.route.substringBefore("/{id}")) -> NoteEditDestination.route
//        else -> NavigationDestination.valueOf(route)
//    }
//}

@Composable
fun SimpleNotesApp(navController: NavHostController = rememberNavController()) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = getScreenFromRoute(backStackEntry?.destination?.route)
    val viewModel: NoteEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
    Scaffold(
        topBar = {
//            SimpleNotesAppBar(
////                currentScreen = currentScreen,
//                canNavigateBack = navController.previousBackStackEntry != null ,
//                navigateUp = { navController.navigateUp() })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(
                route = NoteEditDestination.routeWithArgs,
                arguments = listOf(navArgument(NoteEditDestination.noteIdArg) {
                type = NavType.IntType
            }))  {
                NoteEditScreen(navController = navController)
            }
            composable(route = Screen.Add.route) {
                NoteAddScreen(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleNotesPreview() {
    SimpleNotesAppTheme {
        SimpleNotesApp()
    }
}