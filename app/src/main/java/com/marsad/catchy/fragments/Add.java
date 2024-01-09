package com.marsad.catchy.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.marsad.catchy.R;
import com.marsad.catchy.verify.verify_decription;
import com.marsad.catchy.adapter.GalleryAdapter;
import com.marsad.catchy.model.GalleryImages;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Add extends Fragment {

    Uri imageUri;

    Dialog dialog;

    private EditText descET;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ImageButton backBtn, nextBtn;
    private List<GalleryImages> list;
    private GalleryAdapter adapter;
    private FirebaseUser user;
    private Button addbtn;

    private boolean check;
    private  String[] offensiveWords = {"buồi","buoi","dau buoi","daubuoi","caidaubuoi","nhucaidaubuoi","dau boi","bòi","dauboi","caidauboi","đầu bòy","đầu bùi","dau boy","dauboy","caidauboy","b","cặc","cak","kak","kac","cac","concak","nungcak","bucak","caiconcac","caiconcak","cu","cặk","cak","dái","giái","zái","kiu","cứt","cuccut","cutcut","cứk","cuk","cười ỉa","cười ẻ","đéo","đếch","đếk","dek","đết","đệt","đách","dech","d'","deo","d'","đel","đél","del","dell ngửi","dell ngui","dell chịu","dell chiu","dell hiểu","dell hieu","dellhieukieugi","dell nói","dell noi","dellnoinhieu","dell biết","dell biet","dell nghe","dell ăn","dell an","dell được","dell duoc","dell làm","dell lam","dell đi","dell di","dell chạy","dell chay","deohieukieugi","địt","đm","dm","đmm","dmm","đmmm","dmmm","đmmmm","dmmmm","đmmmmm","dmmmmm","đcm","dcm","đcmm","dcmm","đcmmm","dcmmm","đcmmmm","dcmmmm","đệch","đệt","dit","dis","diz","đjt","djt","địt mẹ","địt mịe","địt má","địt mía","địt ba","địt bà","địt cha","địt con","địt bố","địt cụ","dis me","disme","dismje","dismia","dis mia","dis mie","đis mía","đis mía","ditmemayconcho","ditmemay","ditmethangoccho","ditmeconcho","dmconcho","dmcs","ditmecondi","ditmecondicho","đụ","đụ mẹ","đụ mía","đụ mía","đụ má","đụ cha","đụ bà","đú cha","đú con mẹ","đú má","đú mẹ","đù cha","đù má","đù mẹ","đù mía","đù mía","đù mía","đù mía","đù mía","đù mía","đìu","đờ mờ","đê mờ","đờ ma ma","đờ mama","đê mama","đề mama","đê ma ma","đề ma ma","dou","doma","duoma","dou má","duo má","dou ma","đou má","đìu má","á đù","á đìu","đậu mẹ","đậu má","đĩ","di~","đuỹ","điếm","cđĩ","cdi~","đilol","điloz","đilon","diloz","dilol","dilon","condi","condi~","dime","di me","dimemay","condime","condimay","condimemay","con di cho","con di cho'","condicho","bitch","biz","bít chi","con bích","con bic","con bíc","con bít","phò","4","lồn","l`","loz","lìn","nulo","ml","matlon","cailon","matlol","matloz","thml","thangmatlon","thangml","đỗn lì","tml","thml","diml","dml","hãm","xàm lol","xam lol","xạo lol","xao lol","con lol","ăn lol","an lol","mát lol","mat lol","cái lol","cai lol","lòi lol","loi lol","ham lol","củ lol","cu lol","ngu lol","tuổi lol","tuoi lol","mõm lol","mồm lol","mom lol","như lol","nhu lol","nứng lol","nung lol","nug lol","nuglol","rảnh lol","ranh lol","đách lol","dach lol","mu lol","banh lol","tét lol","tet lol","vạch lol","vach lol","cào lol","cao lol","tung lol","mặt lol","mát lol","mat lol","xàm lon","xam lon","xạo lon","xao lon","con lon","ăn lon","an lon","mát lon","mat lon","cái lon","cai lon","lòi lon","loi lon","ham lon","củ lon","cu lon","ngu lon","tuổi lon","tuoi lon","mõm lon","mồm lon","mom lon","như lon","nhu lon","nứng lon","nung lon","nug lon","nuglon","rảnh lon","ranh lon","đách lon","dach lon","mu lon","banh lon","tét lon","tet lon","vạch lon","vach lon","cào lon","cao lon","tung lon","mặt lon","mát lon","mat lon","cái lờ","cl","clgt","cờ lờ gờ tờ","cái lề gì thốn","đốn cửa lòng","sml","sapmatlol","sapmatlon","sapmatloz","sấp mặt","sap mat","vlon","vloz","vlol","vailon","vai lon","vai lol","vailol","nốn lừng","vcl","vl","vleu","chịch","chich","vãi","v~","đụ","nứng","nug","đút đít","chổng mông","banh háng","xéo háng","xhct","xephinh","la liếm","đổ vỏ","xoạc","xoac","chich choac","húp sò","fuck","fck","đụ","bỏ bú","buscu","ngu","óc chó","occho","lao cho","láo chó","bố láo","chó má","cờ hó","sảng","thằng chó","thang cho'","thang cho","chó điên","thằng điên","thang dien","đồ điên","sủa bậy","sủa tiếp","sủa đi","sủa càn","mẹ bà","mẹ cha mày","me cha may","mẹ cha anh","mẹ cha nhà anh","mẹ cha nhà mày","me cha nha may","mả cha mày","mả cha nhà mày","ma cha may","ma cha nha may","mả mẹ","mả cha","kệ mẹ","kệ mịe","kệ mịa","kệ mje","kệ mja","ke me","ke mie","ke mia","ke mja","ke mje","bỏ mẹ","bỏ mịa","bỏ mịe","bỏ mja","bỏ mje","bo me","bo mia","bo mie","bo mje","bo mja","chetme","chet me","chết mẹ","chết mịa","chết mja","chết mịe","chết mie","chet mia","chet mie","chet mja","chet mje","thấy mẹ","thấy mịe","thấy mịa","thay me","thay mie","thay mia","tổ cha","bà cha mày","cmn","cmnl","tiên sư nhà mày","tiên sư bố","tổ sư"};

    public Add() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);

        recyclerView.setAdapter(adapter);

        clickListener();

    }
    private void clickListener() {
            adapter.SendImage(picUri -> CropImage.activity(picUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4, 3)
                    .start(getContext(), Add.this));

            nextBtn.setOnClickListener(v -> {
//                if(0==0) {
                if(check==false){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageReference = storage.getReference().child("Post Images/" + System.currentTimeMillis());

                    dialog.show();

                    storageReference.putFile(imageUri)
                            .addOnCompleteListener(task -> {
                                dialog.dismiss();
                                if (task.isSuccessful() && check == false) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> uploadData(uri.toString()));
                                } else {
                                    Toast.makeText(getContext(), "Failed to upload post", Toast.LENGTH_SHORT).show();
                                }
                                });
                }else
                    Toast.makeText(getContext(), "bài đăng chứa nộ dụng nhạy cảm", Toast.LENGTH_SHORT).show();
            });
        }

    private void uploadData(String imageURL) {
        if(verify_decription.containsOffensiveWords(descET.getText().toString(),0.8,offensiveWords)==true) {
            check = true;
            // Display notification and prevent further action
            Toast.makeText(getContext(), "Offensive language detected. Please modify your description.", Toast.LENGTH_SHORT).show();
             // Stops execution of the remaining code
        }
        else{
            check = false;
        }
        if(check==false){
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid()).collection("Post Images");

        String id = reference.document().getId();

        String description = descET.getText().toString();

        List<String> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("description", description);
        map.put("imageUrl", imageURL);
        map.put("timestamp", FieldValue.serverTimestamp());

        map.put("name", user.getDisplayName());
        map.put("profileImage", String.valueOf(user.getPhotoUrl()));

        map.put("likes", list);

        map.put("uid", user.getUid());

        reference.document(id).set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println();
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                });

    }
    }

    private void init(View view) {

        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        backBtn = view.findViewById(R.id.backBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        addbtn = view.findViewById(R.id.verify_btn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "clickCheck: ");
                if(verify_decription.containsOffensiveWords(descET.getText().toString(),0.8,offensiveWords)==false)
                    Toast.makeText(getContext(), "nội dung hợp lệ", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "bài đăng chứa nộ dụng nhạy cảm", Toast.LENGTH_SHORT).show();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.laoding_dialog);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dialog_bg, null));
        dialog.setCancelable(false);


    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().runOnUiThread(() -> Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download");

                            if (file.exists()) {
                                File[] files = file.listFiles();
                                assert files != null;

                                list.clear();

                                for (File file1 : files) {

                                    if (file1.getAbsolutePath().endsWith(".jpg") || file1.getAbsolutePath().endsWith(".png")) {

                                        list.add(new GalleryImages(Uri.fromFile(file1)));
                                        adapter.notifyDataSetChanged();

                                    }

                                }


                            }

                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                assert result != null;
                imageUri = result.getUri();

                Glide.with(getContext())
                        .load(imageUri)
                        .into(imageView);

                imageView.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
            }
        }

    }


//    public void clickCheck(View view) {
//        Log.d("TAG", "clickCheck: ");
//        if(verify_decription.containsOffensiveWords(descET.toString(),0.3,offensiveWords))
//            Toast.makeText(getContext(), "nội dung hợp lệ", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getContext(), "bài đăng chứa nộ dụng nhạy cảm", Toast.LENGTH_SHORT).show();
//    }



}