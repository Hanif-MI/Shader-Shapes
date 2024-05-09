package com.hanif.exproleshader

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer


/* Created by Hanif on 06/05/24 */

const val COORDS_PER_VERTEX_SQUARE = 3
private const val VERTEX_STRIDE: Int = COORDS_PER_VERTEX_SQUARE * 4
private var quadPositionHandle = -1
private var program: Int = -1

val squareCoords = floatArrayOf(
    -.2f, .2f, 0.0f, // top left
    -.2f, -.2f, 0.0f, // bottom left
    .2f, -.2f, 0.0f, // bottom right
    .2f, .2f, 0.0f // top right
)

class Square {

    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX_SQUARE

    private val vertexShaderCode = """
    uniform mat4 uMVPMatrix;
    attribute vec4 vPosition;
    void main() {
       gl_Position =  uMVPMatrix * vPosition;
    }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
        """.trimIndent()
    private val color = floatArrayOf(1f, 0f, 0f, 1f)


    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(squareCoords.size * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(squareCoords)
            position(0)
        }
    }

    private val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    init {

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }


    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)
        quadPositionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX_SQUARE,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )
            GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }


            GLES20.glGetUniformLocation(program,"uMVPMatrix").also {matrix ->
                GLES20.glUniformMatrix4fv(matrix, 1, false, mvpMatrix, 0)
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                drawOrder.size,
                GLES20.GL_SHORT,
                drawListBuffer
            );
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}