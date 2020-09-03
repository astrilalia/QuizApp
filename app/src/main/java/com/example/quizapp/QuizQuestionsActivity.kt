package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswer: Int = 0
    private var mUserName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionList = Constants.getQuestion()
        setQuestion()

        btn_option_one.setOnClickListener(this)
        btn_option_two.setOnClickListener(this)
        btn_option_three.setOnClickListener(this)
        btn_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    private fun setQuestion(){

        val question = mQuestionList!![mCurrentPosition - 1]

        defaultOptionsView()

        if(mCurrentPosition == mQuestionList!!.size){
            btn_submit.text = "FINISH"
        } else {
            btn_submit.text = "Submit"
        }

        progress_bar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition /" + progress_bar.max

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        btn_option_one.text = question.optionOne
        btn_option_two.text = question.optionTwo
        btn_option_three.text = question.optionThree
        btn_option_four.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, btn_option_one)
        options.add(1, btn_option_two)
        options.add(2, btn_option_three)
        options.add(3, btn_option_four)

        for (option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_option_one -> {
                selectedOptionView(btn_option_one, 1)
            }
            R.id.btn_option_two -> {
                selectedOptionView(btn_option_two, 2)
            }
            R.id.btn_option_three -> {
                selectedOptionView(btn_option_three, 3)
            }
            R.id.btn_option_four -> {
                selectedOptionView(btn_option_four, 4)
            }
            R.id.btn_submit -> {
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        } else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWER, mCorrectAnswer)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
                            startActivity(intent)

                        }
                    }
                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswer++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if(mCurrentPosition == mQuestionList!!.size){
                        btn_submit.text = "FINISH"
                    } else {
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                btn_option_one.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                btn_option_two.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                btn_option_three.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                btn_option_four.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun selectedOptionView(btn: Button, selectedOptionNum: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        btn.setTextColor(Color.parseColor("#363A43"))
        btn.setTypeface(btn.typeface, Typeface.BOLD)
        btn.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

}