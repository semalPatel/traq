package com.traq.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

data class ChartDataPoint(
    val x: Float,
    val y: Float
)

@Composable
fun ElevationChart(
    points: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
    label: String = "Elevation"
) {
    ChartWithGradient(
        points = points,
        modifier = modifier,
        lineColor = lineColor,
        fillColor = fillColor,
        label = label
    )
}

@Composable
fun SpeedChart(
    points: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.tertiary,
    fillColor: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
    label: String = "Speed"
) {
    ChartWithGradient(
        points = points,
        modifier = modifier,
        lineColor = lineColor,
        fillColor = fillColor,
        label = label
    )
}

@Composable
private fun ChartWithGradient(
    points: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color,
    fillColor: Color,
    label: String
) {
    if (points.size < 2) return

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val padding = 4.dp.toPx()
            val chartWidth = size.width - padding * 2
            val chartHeight = size.height - padding * 2

            val linePath = Path()
            val fillPath = Path()

            points.forEachIndexed { index, point ->
                val x = padding + point.x * chartWidth
                val y = padding + (1f - point.y) * chartHeight

                if (index == 0) {
                    linePath.moveTo(x, y)
                    fillPath.moveTo(x, size.height)
                    fillPath.lineTo(x, y)
                } else {
                    linePath.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
            }

            val lastX = padding + points.last().x * chartWidth
            fillPath.lineTo(lastX, size.height)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(fillColor, fillColor.copy(alpha = 0f)),
                    startY = 0f,
                    endY = size.height
                )
            )

            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
