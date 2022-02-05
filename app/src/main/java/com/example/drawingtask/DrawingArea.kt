package com.example.drawingtask


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.PointF
import kotlin.math.sqrt


class DrawingArea(context: Context? ,attrs:AttributeSet) : View(context,attrs) {
    private val pathList  = arrayListOf<CustomPath>()
    private var path : CustomPath? = null
    private val paintColor = Paint()
    //Default Values
    private var currentColor = Color.BLACK
    private var currentShape = Shapes.Pen
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    // circle
    private var radius = 0F
    // Arrow
    private var mX = 0f
    private var mY = 0f
    private lateinit var startPoint :PointF
    private lateinit var endPoint :PointF

    //Constructor
    init {
        setUpDrawing()
    }

    // Default Drawing
    private fun setUpDrawing(){
        path = CustomPath(currentColor,currentShape)
        paintColor.isAntiAlias = true
        paintColor.color = Color.BLACK
        paintColor.style = Paint.Style.STROKE
        paintColor.strokeCap = Paint.Cap.ROUND
        paintColor.strokeJoin = Paint.Join.ROUND
        paintColor.strokeWidth = 10f
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionTouchEventX = event?.x!!
        motionTouchEventY = event.y
         when(event.action){
            MotionEvent.ACTION_DOWN -> {
                handleDrawActionDown()
            }
            MotionEvent.ACTION_MOVE-> {
                handleDrawActionMove()
            }
             MotionEvent.ACTION_UP ->{
                 handleDrawActionUp()
             }
            else -> return false
        }
        invalidate()
        return  true
    }

    private fun handleDrawActionDown() {
        when(currentShape){
            Shapes.Pen ->{
                path!!.color = currentColor
                path!!.shape = currentShape
                path!!.moveTo(motionTouchEventX,motionTouchEventY)
            }
            Shapes.Circle ->{
                path!!.color = currentColor
                path!!.shape = currentShape
                path!!.x = motionTouchEventX
                path!!.y = motionTouchEventY
            }
            Shapes.Rec ->{
                path!!.color = currentColor
                path!!.shape = currentShape
                path!!.x = motionTouchEventX
                path!!.y = motionTouchEventY
                path!!.widthRec = (100..300).random().toFloat()
                path!!.heightRec = (50..100).random().toFloat()
                pathList.add(path!!)
            }
            Shapes.Arrow ->{
                path!!.color = currentColor
                path!!.shape = currentShape
                path!!.moveTo(motionTouchEventX,motionTouchEventY)
                mX = motionTouchEventX
                mY = motionTouchEventY
                startPoint = PointF(motionTouchEventX, motionTouchEventY)
                endPoint = PointF()
            }
        }
    }

    private fun handleDrawActionMove() {
        when(currentShape){
            Shapes.Pen ->{
                path!!.lineTo(motionTouchEventX,motionTouchEventY)
                pathList.add(path!!)
            }
            Shapes.Circle->{
                radius =  calculateRadius(path!!.x!!,path!!.y!!,motionTouchEventX,motionTouchEventY)
                path!!.radius = radius
                pathList.add(path!!)
            }
            Shapes.Arrow->{
                mX = motionTouchEventX
                mY = motionTouchEventY
                endPoint.x = motionTouchEventX
                endPoint.y = motionTouchEventY
                path!!.lineTo(mX, mY)
                pathList.add(path!!)
            }
            else ->{}
        }
    }

    private fun handleDrawActionUp(){
        when(currentShape){
            Shapes.Arrow->{
                handleArrowHead()
                pathList.add(path!!)
            }
            else->{}
        }

        path = CustomPath(currentColor,currentShape)
    }

    override fun onDraw(canvas: Canvas?) {
        for (p in pathList) {
            paintColor.color = p.color
            drawByShape(canvas,paintColor,p)
        }
    }

    private fun drawByShape(canvas: Canvas?, paintColor: Paint, p: CustomPath) {
        when(p.shape){
            Shapes.Pen ->{ canvas?.drawPath(p, paintColor)}
            Shapes.Circle ->{ canvas?.drawCircle(p.x!!,p.y!!,p.radius!!,paintColor)}
            Shapes.Rec ->{ canvas?.drawRect(p.x!!,p.y!!, p.x!! + p.widthRec!!, p.y!! + p.heightRec!! , paintColor) }
            Shapes.Arrow->{canvas?.drawPath(p,paintColor)}
        }
    }


    //Change CurrentColor
    fun currentColor(c :Int){
        currentColor = c
        paintColor.color = currentColor
    }

    //Change CurrentShape
    fun currentShape(c :Shapes){
        currentShape = c
    }

    private fun handleArrowHead(){
        //Arrow Head
        val deltaX = endPoint.x - startPoint.x
        val deltaY = endPoint.y - startPoint.y
        val fraction = 0.1.toFloat()
        val pointX1 = startPoint.x + ((1 - fraction) * deltaX + fraction * deltaY)
        val pointy1 = startPoint.y + ((1 - fraction) * deltaY - fraction * deltaX)
        val pointX2 = endPoint.x
        val pointY2 = endPoint.y
        val pointX3 = startPoint.x + ((1 - fraction) * deltaX - fraction * deltaY)
        val pointY3 = startPoint.y + ((1 - fraction) * deltaY + fraction * deltaX)
        path!!.moveTo(pointX1, pointy1)
        path!!.lineTo(pointX2, pointY2)
        path!!.lineTo(pointX3, pointY3)
        path!!.lineTo(pointX1, pointy1)
        path!!.lineTo(pointX1, pointy1)
    }

    private fun calculateRadius(x: Float, y: Float, x1: Float, y1: Float): Float {
        val firstPoint = if (x > x1) { x - x1 } else { x1 - x }
        val secondPoint = if (y > y1) { y - y1 } else { y1 - y }
        val secondSquare = secondPoint * secondPoint
        val firstSquare = firstPoint * firstPoint
        val valueToSqrt = secondSquare + firstSquare
        return sqrt(valueToSqrt) / 2
    }


    internal inner class CustomPath(var color: Int,
                                    var shape:Shapes,
                                    var x: Float?=null,
                                    var y: Float?=null,
                                    var radius:Float?=null,
                                    var widthRec:Float?=null,
                                    var heightRec:Float?=null) : Path()
}