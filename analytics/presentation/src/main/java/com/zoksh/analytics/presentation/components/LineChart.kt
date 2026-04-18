package com.zoksh.analytics.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoksh.analytics.domain.AnalyticsHistoryPoint
import com.zoksh.com.core.presentation.designsystem.RuniqueGreen
import com.zoksh.com.core.presentation.designsystem.RuniqueWhite
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun LineChart(
    dataPoints: List<AnalyticsHistoryPoint>,
    valueSelector: (AnalyticsHistoryPoint) -> Double,
    modifier: Modifier = Modifier,
    selectedPointIndex: Int? = null,
    onPointClick: (Int) -> Unit = {}
) {
    if (dataPoints.isEmpty()) return

    val values = dataPoints.map { valueSelector(it).toFloat() }
    val maxValue = (values.maxOrNull() ?: 1f).coerceAtLeast(1f)
    
    val neonGreen = RuniqueGreen
    val baselineColor = Color(0xFF333333)
    val gridLineColor = Color.White.copy(alpha = 0.15f)
    val highlightLineColor = Color.White.copy(alpha = 0.4f)
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(dataPoints) {
                fun detect(offset: Offset) {
                    val startPadding = 32.dp.toPx()
                    val step = (size.width - startPadding * 2) / (values.size - 1)
                    val index = ((offset.x - startPadding) / step).roundToInt().coerceIn(0, values.size - 1)
                    onPointClick(index)
                }
                detectTapGestures { detect(it) }
            }
            .pointerInput(dataPoints) {
                detectDragGestures { change, _ ->
                    val startPadding = 32.dp.toPx()
                    val step = (size.width - startPadding * 2) / (values.size - 1)
                    val index = ((change.position.x - startPadding) / step).roundToInt().coerceIn(0, values.size - 1)
                    onPointClick(index)
                }
            }
    ) {
        val width = size.width
        val height = size.height
        
        val startPadding = 32.dp.toPx()
        val bottomPadding = 55.dp.toPx()
        val topPadding = 70.dp.toPx()
        val chartHeight = height - topPadding - bottomPadding
        
        val step = (width - startPadding * 2) / (values.size - 1)
        
        fun getX(index: Int) = startPadding + index * step
        fun getY(value: Float) = (height - bottomPadding) - (value / maxValue * chartHeight)

        val path = Path()
        val fillPath = Path()

        drawLine(
            color = baselineColor,
            start = Offset(0f, height - bottomPadding),
            end = Offset(width, height - bottomPadding),
            strokeWidth = 1.2.dp.toPx()
        )

        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E")
            textSize = 10.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }

        val gridIndices = listOf(0, 4, 8, 12) 
        values.forEachIndexed { index, _ ->
            val x = getX(index)
            drawContext.canvas.nativeCanvas.drawText((index + 1).toString(), x, height - 15.dp.toPx(), textPaint)
            if (gridIndices.contains(index)) {
                drawLine(gridLineColor, Offset(x, topPadding / 2), Offset(x, height - bottomPadding), 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
            }
        }

        val firstX = getX(0)
        val firstY = getY(values[0])
        
        path.moveTo(firstX, firstY)
        fillPath.moveTo(firstX, height - bottomPadding)
        fillPath.lineTo(firstX, firstY)

        for (i in 1 until values.size) {
            val pX = getX(i - 1); val pY = getY(values[i - 1])
            val cX = getX(i); val cY = getY(values[i])
            val mX = (pX + cX) / 2; val mY = (pY + cY) / 2
            path.cubicTo(pX + (mX - pX) * 0.5f, pY + 8.dp.toPx(), mX - (mX - pX) * 0.5f, mY - 10.dp.toPx(), mX, mY)
            path.cubicTo(mX + (cX - mX) * 0.5f, mY + 10.dp.toPx(), cX - (cX - mX) * 0.5f, cY - 8.dp.toPx(), cX, cY)
            fillPath.cubicTo(pX + (mX - pX) * 0.5f, pY + 8.dp.toPx(), mX - (mX - pX) * 0.5f, mY - 10.dp.toPx(), mX, mY)
            fillPath.cubicTo(mX + (cX - mX) * 0.5f, mY + 10.dp.toPx(), cX - (cX - mX) * 0.5f, cY - 8.dp.toPx(), cX, cY)
        }

        val lastX = getX(values.size - 1); val lastY = getY(values.last())
        path.cubicTo(lastX + (width - lastX) * 0.5f, lastY + 8.dp.toPx(), lastX + (width - lastX) * 0.8f, lastY - 6.dp.toPx(), width, lastY + 3.dp.toPx())
        fillPath.cubicTo(lastX + (width - lastX) * 0.5f, lastY + 8.dp.toPx(), lastX + (width - lastX) * 0.8f, lastY - 6.dp.toPx(), width, lastY + 3.dp.toPx())
        
        fillPath.lineTo(width, height - bottomPadding)
        fillPath.lineTo(firstX, height - bottomPadding)
        fillPath.close()

        drawPath(fillPath, brush = Brush.verticalGradient(colors = listOf(neonGreen.copy(alpha = 0.55f), neonGreen.copy(alpha = 0.15f), Color.Transparent), startY = topPadding, endY = height - bottomPadding))
        drawPath(path, neonGreen, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))

        if (selectedPointIndex != null && selectedPointIndex in values.indices) {
            val selX = getX(selectedPointIndex); val selY = getY(values[selectedPointIndex])
            drawLine(highlightLineColor, Offset(selX, selY + 12.dp.toPx()), Offset(selX, height - bottomPadding), 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f)))
            
            val txt = "${(values[selectedPointIndex] * 10).roundToInt() / 10.0} km"
            val paint = android.graphics.Paint().apply { color = android.graphics.Color.BLACK; textSize = 12.sp.toPx(); isFakeBoldText = true; textAlign = android.graphics.Paint.Align.CENTER }
            val bnds = android.graphics.Rect(); paint.getTextBounds(txt, 0, txt.length, bnds)
            val tw = bnds.width() + 20.dp.toPx(); val th = bnds.height() + 12.dp.toPx(); val yO = 30.dp.toPx()
            val rect = Rect(selX - tw / 2, selY - th - yO, selX + tw / 2, selY - yO)
            drawRoundRect(RuniqueWhite, Offset(rect.left, rect.top), Size(rect.width, rect.height), androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()))
            drawPath(Path().apply { moveTo(selX, selY - yO + 6.dp.toPx()); lineTo(selX - 6.dp.toPx(), selY - yO); lineTo(selX + 6.dp.toPx(), selY - yO); close() }, RuniqueWhite)
            drawContext.canvas.nativeCanvas.drawText(txt, selX, rect.bottom - 8.dp.toPx(), paint)
        }

        val circleIdx = listOf(1, 3, 5, 7, 8, 10, 12, 14)
        values.forEachIndexed { index, value ->
            if (circleIdx.contains(index) || index == selectedPointIndex) {
                val x = getX(index); val y = getY(value)
                val isSel = index == selectedPointIndex
                drawCircle(RuniqueWhite, if (isSel) 8.dp.toPx() else 4.5.dp.toPx(), Offset(x, y))
                drawCircle(neonGreen, if (isSel) 4.5.dp.toPx() else 2.5.dp.toPx(), Offset(x, y))
            }
        }
    }
}