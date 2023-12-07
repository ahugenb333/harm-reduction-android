package com.ahugenb.hra.goal

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.Utils
import com.ahugenb.hra.Utils.Companion.returnGoalStatus
import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalStatus
import com.ahugenb.hra.tracker.TrackerState

@Composable
fun GoalView(goalState: GoalState, trackerState: TrackerState, navController: NavController) {
    if (trackerState is TrackerState.TrackerStateEmpty) {
        return
    }
    val trackerStateAll = trackerState as TrackerState.TrackerStateAll

    val icon = when(returnGoalStatus(trackerState = trackerStateAll, goalState = goalState)) {
        GoalStatus.RED -> Icons.Default.Warning
        GoalStatus.YELLOW -> Icons.Default.Warning
        GoalStatus.GREEN -> Icons.Default.Star
    }

    val iconColor = when(returnGoalStatus(trackerState = trackerState, goalState = goalState)) {
        GoalStatus.RED -> Color.Red
        GoalStatus.YELLOW -> Color.Yellow
        GoalStatus.GREEN -> Color.Green
    }
    LazyColumn {
        items(goalState.goals.size) { index ->
            GoalListItem(goalState.goals[index], icon, iconColor, index + 1)
        }
    }
    FloatingActionButton(onClick = {
        navController.navigate("goalCreate")
    }) {
        Icon(Icons.Default.Add, contentDescription = "Create Goal")
    }
}

@Composable
fun GoalListItem(goal: Goal, icon: ImageVector, iconColor: Color, goalNumber: Int) {
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
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = "Goal Status")
            }

            // Goal Number and Text
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text("Goal $goalNumber", style = MaterialTheme.typography.h6)
                Text("Type: ${goal.type.name}")
                Text("Period: ${goal.period.name}")
            }

            // Edit Button
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .size(24.dp)
                    .padding(8.dp)
            )
        }
    }
}