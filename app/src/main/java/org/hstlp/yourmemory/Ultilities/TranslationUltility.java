package org.hstlp.yourmemory.Ultilities;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
public class TranslationUltility {
    private Translator translatorEnglish;
    private Translator translatorFrance;
    private Translator translatorSpain;
    private Translator translatorKorea;
    private Translator translatorJapanese;

    // for checking which language we are at
    private Boolean booleanEnglish = true;
    private Boolean booleanFrance = false;
    private Boolean booleanKorean = false;
    private Boolean booleanSpain = false;
    private Boolean booloeanJapan = false;

    public TranslationUltility() {
        TranslatorOptions translatorOptionsEnglish = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();

        TranslatorOptions translatorOptionsFrench = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build();

        TranslatorOptions translatorOptionsKorean = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN)
                .build();

        TranslatorOptions translatorOptionsSpain = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build();

        TranslatorOptions translatorOptionsJappan = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.JAPANESE)
                .build();

        translatorEnglish = Translation.getClient(translatorOptionsEnglish);
        translatorKorea = Translation.getClient(translatorOptionsKorean);
        translatorJapanese = Translation.getClient(translatorOptionsJappan);
        translatorFrance = Translation.getClient(translatorOptionsFrench);
        translatorSpain = Translation.getClient(translatorOptionsSpain);

        downloadModel();
    }

    public void downloadModel() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translatorEnglish.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        booleanEnglish = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        booleanEnglish = false;
                    }
                });

        translatorFrance.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        booleanFrance = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        booleanFrance = false;
                    }
                });

        translatorKorea.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        booleanKorean = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        booleanKorean = false;
                    }
                });

        translatorJapanese.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        booloeanJapan = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        booloeanJapan = false;
                    }
                });
    }

    public void translateFrance(String translateText, TextView tv) {
        if (booleanFrance){
            translatorFrance.translate(translateText)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            tv.setText(s);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tv.setText(e.toString());
                        }
                    });
        }
    }

    public void translateSpain(String translateText, TextView tv) {
        if (booleanSpain){
            translatorSpain.translate(translateText)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            tv.setText(s);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tv.setText(e.toString());
                        }
                    });
        }
    }

    public void translateKorea(String translateText, TextView tv) {
        if (booleanKorean){
            translatorKorea.translate(translateText)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            tv.setText(s);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tv.setText(e.toString());
                        }
                    });
        }
    }

    public void translateJapan(String translateText, TextView tv) {
        if (booloeanJapan){
            translatorJapanese.translate(translateText)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            tv.setText(s);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tv.setText(e.toString());
                        }
                    });
        }
    }
}
