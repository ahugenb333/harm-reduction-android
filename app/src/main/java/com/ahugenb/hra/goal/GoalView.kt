package com.ahugenb.hra.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalStatus

@Composable
fun GoalView(goalState: GoalState, navController: NavController) {
    LazyColumn {
        items(goalState.goals.size) { index ->
            GoalListItem(goalState.goals[index], index + 1)
        }
    }
    FloatingActionButton(onClick = { /*TODO*/ }) {
        
    }
}

@Composable
fun GoalListItem(goal: Goal, goalNumber: Int) {
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
                    .background(getColorForGoalStatus(goal.status)),
                contentAlignment = Alignment.Center
            ) {
                // You can customize the content inside the circle, e.g., display an icon
                when (goal.status) {
                    GoalStatus.PASSING -> Icon(Icons.Default.Star, contentDescription = null)
                    GoalStatus.FAILING -> Icon(Icons.Default.Warning, contentDescription = null)
                }
            }

            // Goal Number and Text
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text("Goal $goalNumber", style = MaterialTheme.typography.h6)
                Text("Type: ${goal.type.name}")
                Text("Unit: ${goal.unit.name}")
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

@Composable
private fun getColorForGoalStatus(status: GoalStatus): Color {
    return when (status) {
        GoalStatus.PASSING -> Color.Green
        GoalStatus.FAILING -> Color.Red
    }
}