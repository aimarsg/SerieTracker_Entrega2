package com.aimarsg.serietracker.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.TrackerScreen

@Composable
public fun AppNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    siguiendo: Boolean
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ){
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,

        ) {
            NavigationRailItem(
                selected = siguiendo,
                onClick = {
                    if (siguiendo){
                        // do nothing
                    }else{
                        navController.navigateUp()
                    }
                },
                label = {
                    Text(text = stringResource(R.string.siguiendo))
                },
                icon = {
                    if (siguiendo){
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = stringResource(
                            R.string.siguiendo)
                        )
                    }else{
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = stringResource(
                            R.string.siguiendo
                        )
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            NavigationRailItem(
                selected = !siguiendo,
                label = {
                    Text(text = stringResource(R.string.pendiente))
                },
                onClick = {
                            if (siguiendo){
                                navController.navigate(TrackerScreen.Pendiente.name)
                            }else{
                                // do nothing
                            }
                          },
                icon = {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(R.string.pendiente))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun navigationRailItemPreview(){
    AppNavigationRail(
        navController = NavHostController(
            LocalContext.current
        ), siguiendo = false
    )
}
