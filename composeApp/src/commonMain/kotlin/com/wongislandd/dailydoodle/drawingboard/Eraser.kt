/*
* Converted using https://composables.com/svgtocompose
*/

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Eraser: ImageVector
	get() {
		if (_Eraser != null) {
			return _Eraser!!
		}
		_Eraser = ImageVector.Builder(
            name = "Eraser",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFFE8EAED)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(690f, 720f)
				horizontalLineToRelative(190f)
				verticalLineToRelative(80f)
				horizontalLineTo(610f)
				lineToRelative(80f, -80f)
				close()
				moveToRelative(-500f, 80f)
				lineToRelative(-85f, -85f)
				quadToRelative(-23f, -23f, -23.5f, -57f)
				reflectiveQuadToRelative(22.5f, -58f)
				lineToRelative(440f, -456f)
				quadToRelative(23f, -24f, 56.5f, -24f)
				reflectiveQuadToRelative(56.5f, 23f)
				lineToRelative(199f, 199f)
				quadToRelative(23f, 23f, 23f, 57f)
				reflectiveQuadToRelative(-23f, 57f)
				lineTo(520f, 800f)
				horizontalLineTo(190f)
				close()
				moveToRelative(296f, -80f)
				lineToRelative(314f, -322f)
				lineToRelative(-198f, -198f)
				lineToRelative(-442f, 456f)
				lineToRelative(64f, 64f)
				horizontalLineToRelative(262f)
				close()
				moveToRelative(-6f, -240f)
				close()
			}
		}.build()
		return _Eraser!!
	}

private var _Eraser: ImageVector? = null
