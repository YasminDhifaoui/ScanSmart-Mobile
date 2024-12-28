package com.example.detectionvisage;

/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceLandmark;

import java.util.List;

/**
 * View which displays a bitmap containing a face along with overlay graphics
 * that identify the locations of detected facial landmarks.
 */
public class FaceView extends View {

	private Bitmap mBitmap;
	private List<Face> mFaces;

	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public void setContent(Bitmap bitmap, List<Face> faces) {
		mBitmap = bitmap;
		mFaces = faces;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if ((mBitmap != null)) {
			double scale = drawBitmap(canvas);
			if (mFaces != null)
				afficherFaces(canvas, scale);
		}
	}
	private double drawBitmap(Canvas canvas) {
		double viewWidth = canvas.getWidth();
		double viewHeight = canvas.getHeight();
		double imageWidth = mBitmap.getWidth();
		double imageHeight = mBitmap.getHeight();
		double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);
		Rect destBounds = new Rect(0, 0, (int) (imageWidth * scale), (int) (imageHeight * scale));
		canvas.drawBitmap(mBitmap, null, destBounds, null);
		return scale;
	}
	private void afficherFaces(Canvas canvas, double scale) {
		Paint paint = new Paint();

		for (int i = 0; i < mFaces.size(); ++i) {
			Face face = mFaces.get(i);

				afficherCadre(canvas, scale, paint, face);
				afficherPointInterest(canvas, scale, paint, face);
				afficherEtat(canvas, scale, paint, face);

		}
	}
	private void afficherCadre(Canvas canvas, double scale, Paint paint, Face face) {
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(3);

		float xTL = (float) (face.getBoundingBox().left* scale);
		float yTL = (float) (face.getBoundingBox().top * scale);
		float xBR = (float) (face.getBoundingBox().right* scale);
		float yBR = (float) (face.getBoundingBox().bottom* scale);

//		float width = (float) ((face.getBoundingBox().right-face.getBoundingBox().left) * scale);
//		float height = (float) ((face.getBoundingBox().bottom-face.getBoundingBox().top) * scale);
//		float xBR = xTL + width;
//		float yBR = yTL + height;
		canvas.drawRect(xTL, yTL, xBR, yBR, paint);

	}
	private void afficherPointInterest(Canvas canvas, double scale, Paint paint, Face face) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(3);
		for ( FaceLandmark landmark : face.getAllLandmarks()) {
			int cx = (int) (landmark.getPosition().x * scale);
			int cy = (int) (landmark.getPosition().y * scale);
			canvas.drawCircle(cx, cy, 5, paint);
		}

	}



	private void afficherEtat(Canvas canvas, double scale, Paint paint, Face face) {
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.YELLOW);
		paint.setStrokeWidth(1);
		paint.setTextSize(20.0f);

		float xTL = (float) (face.getBoundingBox().left* scale);
		float yTL = (float) (face.getBoundingBox().top * scale);

		String desc = "id: " + face.getTrackingId();
		desc += " h:" + String.format("%.2f", face.getSmilingProbability());
		desc += " r:" + String.format("%.2f", face.getRightEyeOpenProbability());
		desc += " l:" + String.format("%.2f", face.getLeftEyeOpenProbability());

		canvas.drawText(desc, xTL, yTL, paint);
	}

}
