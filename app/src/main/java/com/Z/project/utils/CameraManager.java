package com.Z.project.utils;

import android.hardware.Camera;

/**
 * Created by macbook on 2018/1/13.
 */

public class CameraManager {
    private Camera camera;
    public CameraManager (Camera camera){
        this.camera=camera;
    }
    public boolean flashControlHandler(){
        Camera.Parameters parameters=camera.getParameters();
        if(Camera.Parameters.FLASH_MODE_OFF.equals(parameters.getFlashMode())){
            turnOn(parameters);
            return true;
        }

        else if(Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())){
            turnOff(parameters);

        }
        return false;
    }

    private void turnOn(Camera.Parameters parameters) {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }
    private void turnOff(Camera.Parameters parameters) {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }
}

