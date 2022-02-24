package dev.baseio.googlecalendar.uidashboard.ui

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import dev.baseio.googlecalendar.commonui.reusable.animateDrag
import dev.baseio.googlecalendar.commonui.theme.GoogleCalendarColorProvider
import dev.baseio.googlecalendar.commonui.theme.GoogleCalendarSurface
import dev.baseio.googlecalendar.commonui.theme.GoogleCalendarTheme
import dev.baseio.googlecalendar.commonui.theme.GoogleCalendarTypography
import dev.baseio.googlecalendar.navigator.ComposeNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(
  ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class,
  androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
fun DashboardUI(composeNavigator: ComposeNavigator) {
  GoogleCalendarTheme {
    val colors = GoogleCalendarColorProvider.colors

    val view = LocalView.current

    SideEffect {
      (view.context as Activity).window.statusBarColor = colors.appBarColor.toArgb()
      (view.context as Activity).window.navigationBarColor = colors.uiBackground.toArgb()
      ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !colors.isDark
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val monthExpanded = remember {
      mutableStateOf(false)
    }
    val calendarHeight = LocalConfiguration.current.screenHeightDp.times(0.3f).dp
    val calendarHeightPx = with(LocalDensity.current) { calendarHeight.toPx() }

    NavigationDrawer(
      drawerContent = {
        DashboardDrawer()
      },
      drawerState = drawerState,
      drawerContainerColor = GoogleCalendarColorProvider.colors.appBarColor,
      modifier = Modifier.padding(),
    ) {
      Scaffold(
        containerColor = GoogleCalendarColorProvider.colors.uiBackground,
        contentColor = GoogleCalendarColorProvider.colors.textPrimary,
        modifier = Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
        topBar = {
          DashboardAppBar({
            switchDrawer(scope, drawerState)
          }, {
            monthExpanded.value = !monthExpanded.value
          })
        },
      ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
          GoogleCalendarSurface {
            Column(
              modifier = Modifier
                .fillMaxSize()
            ) {
              AnimatedVisibility(monthExpanded.value) {
                CalendarMonthView()
              }
              if (monthExpanded.value) {
                Box {
                  CalendarCards()
                  Box(
                    Modifier
                      .fillMaxSize()
                      .animateDrag({
                        monthExpanded.value = false
                      }, {
                        monthExpanded.value = true
                      })
                  )
                }
              } else {
                CalendarCards()
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CalendarMonthView() {
  DashboardMonthView(Modifier.fillMaxWidth())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarCards() {
  LazyColumn(
    modifier = Modifier
  ) {
    items(5) {
      Card(
        modifier = Modifier
          .height(250.dp)
          .fillMaxWidth()
          .padding(16.dp),
        containerColor = GoogleCalendarColorProvider.colors.buttonColor
      ) {
        Text(
          text = "Event",
          style = GoogleCalendarTypography.subtitle1.copy(GoogleCalendarColorProvider.colors.buttonTextColor)
        )
      }
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
private fun switchDrawer(
  scope: CoroutineScope,
  drawerState: DrawerState
) {
  scope.launch {
    if (drawerState.isClosed) {
      drawerState.open()
    } else {
      drawerState.close()
    }
  }
}




