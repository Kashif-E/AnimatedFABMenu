package com.geetmusic.geet.ui.libraryScreens.playlist

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.geetmusic.geet.R

val itemsList = listOf(
FabItem(
icon = R.drawable.round_shuffle_24,
text = R.string.shuffle
),
FabItem(
icon = R.drawable.round_play_arrow_24,
text = R.string.play_all
)
)

@Composable
fun AnimatedFabMenu(
	@DrawableRes icon: Int = R.drawable.round_playlist_play_24,
	@StringRes text: Int = R.string.play,
	items: List<FabItem> = itemsList,
	onItemClick: (FabItem) -> Unit = {},
	fabColor: Color = MaterialTheme.colorScheme.tertiary,
	menuColor: Color = MaterialTheme.colorScheme.surfaceVariant,
	expandedWidth: Dp = 140.dp,
){

	var isFabExpanded by remember {
		mutableStateOf(false)
	}

	val fabTransition = updateTransition(targetState = isFabExpanded, label = "fabTransition")

	val expandFabHorizontally by fabTransition.animateDp(
		transitionSpec = {
			tween(300, if(isFabExpanded) 0 else 50, LinearEasing)
		},
		label = "expandVertically",
		targetValueByState = {isExpanded ->
			if (isExpanded) expandedWidth else 75.dp
		}
	)

	val shrinkFabVertically by fabTransition.animateDp(
		transitionSpec = {
			tween(300, if(isFabExpanded) 0 else 50, LinearEasing)
		},
		label = "shrinkHorizontally",
		targetValueByState = {isExpanded ->
			if (isExpanded) 60.dp else 75.dp
		}
	)

	val menuHeight by fabTransition.animateDp(
		transitionSpec = {
			tween(300, if(isFabExpanded) 0 else 50, LinearEasing)
		},
		label = "menuHeight",
		targetValueByState = {isExpanded ->
			if (isExpanded) (items.size * 50).dp else 0.dp
		}
	)

	ElevatedCard(
		modifier = Modifier.padding(4.dp),
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.elevatedCardElevation(4.dp),
		colors = CardDefaults.elevatedCardColors().copy(
			containerColor = menuColor
		)
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceEvenly,
			modifier = Modifier
				.height(menuHeight)
		) {
			items.forEach { item ->
				AnimatedVisibility(
					visible = isFabExpanded,
					label = "${item.text}",
					enter = fadeIn(tween(300, 150, LinearEasing)),
					exit = fadeOut(tween(200, 50, LinearEasing)) + scaleOut(),
				) {
					Row(
						modifier = Modifier
							.width(expandFabHorizontally)
							.clickable { onItemClick(item) },
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							painterResource(id = item.icon),
							contentDescription = null,
							modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
							tint = MaterialTheme.colorScheme.primary
						)
						Spacer(modifier = Modifier.weight(1f))
						Text(
							text = stringResource(id = item.text),
							modifier = Modifier.padding(end = 16.dp, top = 12.dp, bottom = 12.dp),
							fontWeight = FontWeight.Bold,
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}

		FloatingActionButton(
			elevation = FloatingActionButtonDefaults.elevation(4.dp),
			shape = RoundedCornerShape(16.dp),
			containerColor = fabColor,
			onClick = { isFabExpanded = !isFabExpanded },
			modifier = Modifier
				.width(expandFabHorizontally)
				.height(shrinkFabVertically)
		) {
			AnimatedContent(
				isFabExpanded,
				transitionSpec = {
					when(targetState){
						true -> {
							(
								scaleIn(
									tween(100, 300, FastOutSlowInEasing)
								) + fadeIn(
									tween(250, 300, LinearEasing)
								)
							) .togetherWith(
								fadeOut(
									tween(10, 0, LinearEasing)
								)
							)
						}
						false -> {
							fadeIn(
								tween(250, 200, FastOutSlowInEasing)
							).togetherWith(
								fadeOut(
									tween(10, 0, LinearEasing),
								)
							)
						}
					}
				},
				label = "FabContent"
			){ isExpanded ->
				when(isExpanded){
					true -> {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.Center,
						) {
							Spacer(modifier = Modifier.size(16.dp))
							Icon(
								painter = painterResource(id = icon),
								contentDescription = null,
								modifier = Modifier
									.size(40.dp)
							)
							Spacer(modifier = Modifier.weight(1f))
							Text(
								text = stringResource(id = text),
								modifier = Modifier.padding(end = 16.dp),
								fontWeight = FontWeight.Bold,
								style = MaterialTheme.typography.headlineSmall
							)
						}
					}

					false -> {
						Icon(
							painter = painterResource(id = icon),
							contentDescription = null,
							modifier = Modifier
								.size(40.dp)
						)
					}
				}
			}
		}
	}

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FabPreview(){
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
		AnimatedFabMenu()
	}
}

data class FabItem(
	@DrawableRes val icon: Int,
	@StringRes val text: Int
)
