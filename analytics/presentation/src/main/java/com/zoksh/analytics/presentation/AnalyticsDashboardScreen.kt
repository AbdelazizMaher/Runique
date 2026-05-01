@file:OptIn(ExperimentalMaterial3Api::class)

package com.zoksh.analytics.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoksh.analytics.domain.AnalyticsHistoryPoint
import com.zoksh.analytics.presentation.components.AnalyticsCard
import com.zoksh.analytics.presentation.components.LineChart
import com.zoksh.com.core.presentation.designsystem.ArrowRightIcon
import com.zoksh.com.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme
import com.zoksh.com.core.presentation.designsystem.components.RuniqueScaffold
import com.zoksh.com.core.presentation.designsystem.components.RuniqueToolbar
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime

@Composable
fun AnalyticsDashboardScreenRoot(
    onBackClicked: () -> Unit,
    viewModel: AnalyticsDashboardViewModel = koinViewModel()
) {
    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                AnalyticsAction.OnBackClicked -> onBackClicked()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun AnalyticsDashboardScreen(
    state: AnalyticsDashboardState,
    onAction: (AnalyticsAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                scrollBehavior = scrollBehavior,
                title = stringResource(id = R.string.analytics),
                onBackClicked = {
                    onAction(AnalyticsAction.OnBackClicked)
                }
            )
        }
    ) { paddingValues ->
        if (state.totalDistanceRun.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(16.dp),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "total_stats") {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnalyticsCard(
                            title = stringResource(id = R.string.total_distance_run),
                            value = state.totalDistanceRun,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AnalyticsCard(
                            title = stringResource(id = R.string.total_time_run),
                            value = state.totalTimeRun,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item(key = "fastest_run") {
                    AnalyticsCard(
                        title = stringResource(id = R.string.fastest_ever_run),
                        value = state.fastestEverRun,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.history.isNotEmpty()) {
                    item(key = "distance_chart") {
                        ChartCard(
                            title = stringResource(id = R.string.avg_distance_over_time),
                            subtitle = state.selectedDistanceDate,
                            chart = {
                                LineChart(
                                    dataPoints = state.history,
                                    valueSelector = { it.distanceMeters / 1000.0 },
                                    selectedPointIndex = state.selectedDistanceIndex,
                                    onPointClick = { index ->
                                        onAction(AnalyticsAction.OnDistancePointSelected(index))
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                )
                            }
                        )
                    }
                    item(key = "pace_chart") {
                        ChartCard(
                            title = stringResource(id = R.string.avg_pace_over_time),
                            subtitle = state.selectedPaceDate,
                            chart = {
                                LineChart(
                                    dataPoints = state.history,
                                    valueSelector = { it.paceMinPerKm },
                                    selectedPointIndex = state.selectedPaceIndex,
                                    onPointClick = { index ->
                                        onAction(AnalyticsAction.OnPacePointSelected(index))
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartCard(
    title: String,
    subtitle: String,
    chart: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Icon(
                imageVector = ArrowRightIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        chart()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = KeyboardArrowDownIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun AnalyticsDashboardScreenRootScreenPreview() {
    RuniqueTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsDashboardState(
                totalDistanceRun = "1532 km",
                totalTimeRun = "3d 12h 54min",
                fastestEverRun = "25 km/h",
                avgDistance = "6.6 km",
                avgPace = "07:10",
                selectedDistanceDate = "Mar 2024",
                selectedPaceDate = "Mar 2024",
                history = List(15) { i ->
                    AnalyticsHistoryPoint(
                        dateTimeUtc = ZonedDateTime.now().plusDays(i.toLong()),
                        distanceMeters = (4000 + (Math.sin(i.toDouble()) * 2000)).toInt(),
                        paceMinPerKm = 5.0 + (Math.cos(i.toDouble()) * 2.0)
                    )
                }
            ),
            onAction = {}
        )
    }
}