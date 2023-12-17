package com.ahugenb.hra.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalStatus
import com.ahugenb.hra.home.list.NavScreen
import com.ahugenb.hra.tracker.TrackerState
import com.ahugenb.hra.tracker.getActualValue

@Composable
fun GoalView(goalList: MutableList<Goal>, trackerState: TrackerState.TrackerStateAll, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn {
            items(goalList.size) { index ->
                GoalListItem(goalList[index], trackerState, index + 1)
            }
        }
        FloatingActionButton(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            onClick = { navController.navigate(NavScreen.SCREEN_GOAL_DETAIL.title) }) {
            Icon(Icons.Default.Add, contentDescription = "Add Goal")
        }
    }
}

@Composable
fun GoalListItem(goal: Goal, trackerState: TrackerState.TrackerStateAll, goalNumber: Int) {
    val actualValue =
        trackerState.getActualValue(goal)
    val goalStatus = goal.getGoalStatus(actualValue)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle item click if needed */ },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Colored Circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(getColorForGoalStatus(goalStatus)),
                contentAlignment = Alignment.Center
            ) {
                // You can customize the content inside the circle, e.g., display an icon

            }

            // Goal Number and Text
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text("Goal $goalNumber", style = MaterialTheme.typography.h6)
                Text("Unit: ${goal.unit.unitName}")
                Text("Period: ${goal.period.periodName}")
            }

            // Edit Button
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun getColorForGoalStatus(status: GoalStatus): Color =
    when (status) {
        GoalStatus.GREEN -> Color.Green
        GoalStatus.RED -> Color.Red
        GoalStatus.YELLOW -> Color.Yellow
    }