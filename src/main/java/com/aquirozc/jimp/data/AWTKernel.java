package com.aquirozc.jimp.data;

public enum AWTKernel {
	
	HIGH_PASS_KERNEL(new float[] {
			-1f, -1f, -1f,
	        -1f,  9f, -1f,
	        -1f, -1f, -1f
	}),
	
	SHARPER_KERNEL(new float[] { 
			0, -1f, 0 ,
			-1f, 5f, -1f,
			0, -1f, 0
	}),
	
	EMBOSS_KERNEL(new float[] {
			-2f, -1f, 0f,
		    -1f, 1f, 1f,
		    0f, 1f, 2f
	}),
	
	GAUSS_KERNEL(new float[] {
			1/16f, 1/8f, 1/16f,
		    1/8f, 1/4f, 1/8f,
		    1/16f, 1/8f, 1/16f
	}),
	
	LAPLACE_KERNEL(new float[] {
			1f,4f,1f,
	        4f,-20f,4f,
	        1f,4f,1f
	});
	
	private final float[] instance;
	
	private AWTKernel (float[] instance) {
		
		this.instance = instance;
	}
	
	public float[] getInstance() {
		return instance;
	}

}