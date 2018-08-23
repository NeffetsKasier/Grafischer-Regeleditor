package com.neffets.grafischerregeleditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class ftpUploaderClient {
    public FTPClient mFTPClient = null;
    //String-Variablen für jeweilige Settings in SharedPreference (valuekey)
    private static final String KEY_OPENHAB_IP = "openhab_ip", KEY_OPENHAB_USER = "openhab_user", KEY_OPENHAB_PASSWORD = "openhab_password", KEY_OPENHAB_VERZEICHNIS = "openhab_verzeichnis";
    private String openhab_ip, openhab_user, openhab_password, openhab_verzeichnis;
    private int port = 21;

    public boolean ftpConnect(Context context) {

        SharedPreferences sp_openhab_ip = context.getSharedPreferences(KEY_OPENHAB_IP, 0);
        openhab_ip = sp_openhab_ip.getString(KEY_OPENHAB_IP,"");

        SharedPreferences sp_openhab_user = context.getSharedPreferences(KEY_OPENHAB_USER, 0);
        openhab_user = sp_openhab_user.getString(KEY_OPENHAB_USER, "");

        SharedPreferences sp_gatewayPassword= context.getSharedPreferences(KEY_OPENHAB_PASSWORD, 0);
        openhab_password = sp_gatewayPassword.getString(KEY_OPENHAB_PASSWORD, "");

        SharedPreferences sp_gatewayVerz= context.getSharedPreferences(KEY_OPENHAB_VERZEICHNIS, 0);
        openhab_verzeichnis = sp_gatewayVerz.getString(KEY_OPENHAB_VERZEICHNIS, "");


        try {
            mFTPClient = new FTPClient();
            mFTPClient.connect(openhab_ip, port);
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                boolean status = mFTPClient.login(openhab_user, openhab_password);
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.changeWorkingDirectory(openhab_verzeichnis);
                return status;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error: could not connect to host " + openhab_ip);

        }
        return false;
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }
        return false;
    }

    public boolean ftpUpload(File srcFile, String desFileName) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFile);
            // change working directory to the destination directory
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload failed: " + e);
        }
        return status;
    }
}
