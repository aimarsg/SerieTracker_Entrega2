package com.aimarsg.serietracker.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.TrackerScreen


/**
 * Bottombar of the app, to be shown on the portrait mode
 * it has a bottom for both main screens of the app
 * @param navController: navController of the app
 * @param siguiendo: boolean that indicates wich one is the current screen.
 *  [true] means 'Following, [False] means 'pending'
 */
@Composable
public fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    siguiendo: Boolean,
    //setSiguiendo: (Boolean) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        //modifier = modifier.border(1.dp, Color.Gray)
    ) {
        NavigationBarItem(
            icon = {
                if (siguiendo){
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = stringResource(R.string.siguiendo))
                }else{
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = stringResource(
                        R.string.siguiendo
                    )
                    )
                }
            },
            label = {
                Text(text = stringResource(R.string.siguiendo))
            },
            selected = siguiendo,
            onClick = {
                if (siguiendo){
                    // do nothing
                }else{
                    navController.navigateUp()
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(R.string.pendiente))
            },
            label = {
                Text(text = stringResource(R.string.pendiente))
            },
            selected = !siguiendo,
            onClick = {
                if (siguiendo){
                    navController.navigate(TrackerScreen.Pendiente.name)
                }else{
                    // do nothing
                }
            }
        )
    }
}