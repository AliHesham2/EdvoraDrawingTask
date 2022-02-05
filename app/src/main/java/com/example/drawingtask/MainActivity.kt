package com.example.drawingtask

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RadioGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible


class MainActivity : AppCompatActivity() {
    private lateinit var colorPaletteContainer : ConstraintLayout
     private lateinit var colors : RadioGroup
     private lateinit var view : DrawingArea
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Access the views by id
        val drawItem = findViewById<RadioGroup>(R.id.draw_item)
        val drawingArea =  findViewById<ConstraintLayout>(R.id.drawing_area)
        colors = findViewById(R.id.colors)
        view = findViewById(R.id.drawing_view)
        colorPaletteContainer = findViewById(R.id.container)



        //Handle clicks on empty DrawArea
        drawingArea.setOnClickListener {
           alert()
        }


        //Handle the change of selected drawing item
        drawItem.setOnCheckedChangeListener { _, checkedId ->
            handlePaletteHide(checkedId)
            when(checkedId){
                R.id.radioButton ->  {  view.currentShape(Shapes.Pen)    }
                R.id.radioButton2 -> {  view.currentShape(Shapes.Arrow)  }
                R.id.radioButton3 -> {  view.currentShape(Shapes.Rec)    }
                R.id.radioButton4 -> { view.currentShape(Shapes.Circle)  }
                R.id.radioButton5 -> {
                    showPalette()
                    hideDrawingArea()
                }
            }
        }

        //Handle the change of selected Color
        colors.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.radioButton6 ->{
                    view.currentColor(Color.RED)
                }
                R.id.radioButton7 ->{
                    view.currentColor(Color.GREEN)
                }
                R.id.radioButton8 ->{
                    view.currentColor(Color.BLUE)
                }
                R.id.radioButton9 ->{
                    view.currentColor(Color.BLACK)
                }
            }
        }
    }
    //Hide when click on another tool
    private fun handlePaletteHide(checkedId: Int) {
        if (checkedId != R.id.radioButton5 && colorPaletteContainer.isVisible){
            hidePalette()
            showDrawingArea()
        }
    }

    //hide Color palette
      private fun hidePalette(){
        colors.visibility = GONE
      }
    //show Color palette
      private fun showPalette(){
        colors.visibility = VISIBLE
      }
     //show Drawing Area
     private fun showDrawingArea(){
         view.visibility = VISIBLE
     }
     //hide Drawing Area
     private fun hideDrawingArea(){
         view.visibility = GONE
     }
     private fun alert(){
         Toast.makeText(this,this.resources.getString(R.string.SELECT),Toast.LENGTH_LONG).show()
     }
}