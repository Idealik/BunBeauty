package com.bunbeauty.ideal.myapplication.createService;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ideal.myapplication.R;
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.User;
import com.bunbeauty.ideal.myapplication.fragments.PremiumElement;
import com.bunbeauty.ideal.myapplication.fragments.ServicePhotoElement;
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.Photo;
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.Service;
import com.bunbeauty.ideal.myapplication.helpApi.PanelBuilder;
import com.bunbeauty.ideal.myapplication.helpApi.WorkWithTimeApi;
import com.bunbeauty.ideal.myapplication.fragments.CategoryElement;
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.db.DBHelper;
import com.bunbeauty.ideal.myapplication.other.IPremium;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddingService extends AppCompatActivity implements View.OnClickListener, IPremium {

    private static final String SERVICE_ID = "service id";
    private static final String STATUS_USER_BY_SERVICE = "status UserCreateService";

    private static final int PICK_IMAGE_REQUEST = 71;
    private static final String SERVICE_PHOTO = "service photo";
    private static final String PHOTOS = "photos";
    private static final String PHOTO_LINK = "photo link";
    private static final String CODES = "codes";
    private static final String CODE = "code";
    private static final String COUNT = "count";

    private static final int MAX_COUNT_OF_IMAGES = 10;

    private EditText nameServiceInput;
    private EditText costAddServiceInput;
    private EditText descriptionServiceInput;
    private EditText addressServiceInput;
    private LinearLayout premiumLayout;

    private TextView premiumText;
    private TextView noPremiumText;
    //храним ссылки на картинки в хранилище
    private ArrayList<Uri> fpath;

    private FragmentManager manager;
    private Service service;
    private boolean isPremiumLayoutSelected;
    private String premiumDate;
    private DBHelper dbHelper;
    private CategoryElement categoryElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_service);

        init();
    }

    private void init() {
        Button addServicesBtn = findViewById(R.id.addServiceAddServiceBtn);
        TextView serviceImage = findViewById(R.id.servicePhotoAddServiceImage);
        nameServiceInput = findViewById(R.id.nameAddServiceInput);
        costAddServiceInput = findViewById(R.id.costAddServiceInput);
        descriptionServiceInput = findViewById(R.id.descriptionAddServiceInput);
        addressServiceInput = findViewById(R.id.addressAddServiceInput);
        premiumText = findViewById(R.id.yesPremiumAddServiceText);
        noPremiumText = findViewById(R.id.noPremiumAddServiceText);
        premiumLayout = findViewById(R.id.premiumAddServiceLayout);

        manager = getSupportFragmentManager();
        PremiumElement premiumElement = new PremiumElement();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.premiumAddServiceLayout, premiumElement);

        categoryElement = new CategoryElement(this);
        transaction.add(R.id.categoryAddServiceLayout, categoryElement);

        transaction.commit();

        isPremiumLayoutSelected = false;
        dbHelper = new DBHelper(this);
        fpath = new ArrayList<>();
        service = new Service();
        service.setIsPremium(false);
        premiumDate = "1970-01-01 00:00:00";
        addServicesBtn.setOnClickListener(this);
        serviceImage.setOnClickListener(this);
        premiumText.setOnClickListener(this);
        noPremiumText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addServiceAddServiceBtn:
                if (isFullInputs()) {
                    if (!service.setName(nameServiceInput.getText().toString().trim())) {
                        Toast.makeText(
                                this,
                                "Имя сервиса должно содержать только буквы",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }

                    service.setDescription(descriptionServiceInput.getText().toString().trim());

                    if (!service.setCost(costAddServiceInput.getText().toString().trim())) {
                        Toast.makeText(
                                this,
                                "Цена не может содержать больше 8 символов",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }

                    String category = categoryElement.getCategory();

                    if (category.equals("выбрать категорию")) {
                        Toast.makeText(
                                this,
                                "Не выбрана категория",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String address = addressServiceInput.getText().toString().trim();
                    if (address.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Не указан адрес",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    service.setUserId(getUserId());
                    service.setCategory(category);
                    service.setAddress(address);
                    service.setCountOfRates(0);
                    service.setTags(categoryElement.getTagsArray());
                    //less than 10 images
                    if (fpath.size() <= MAX_COUNT_OF_IMAGES) {
                        uploadService(service);
                    }
                    else {
                        attentionMoreTenImages();
                        break;
                    }
                } else {
                    Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.servicePhotoAddServiceImage:
                chooseImage();
                break;
            case R.id.noPremiumAddServiceText:
                showPremium();
                break;
            case R.id.yesPremiumAddServiceText:
                showPremium();
                break;
            default:
                break;
        }
    }

    private void uploadService(Service service) {
        WorkWithTimeApi workWithTimeApi = new WorkWithTimeApi();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference serviceRef = database.getReference(User.Companion.getUSERS()).child(service.getUserId()).child(Service.SERVICES);

        Map<String, Object> items = new HashMap<>();
        Map<String, String> tagsMap = new HashMap<>();
        items.put(Service.NAME, service.getName().toLowerCase());
        items.put(Service.AVG_RATING, 0);
        items.put(Service.COST, service.getCost());
        items.put(Service.DESCRIPTION, service.getDescription());
        items.put(Service.IS_PREMIUM, premiumDate);
        items.put(Service.CATEGORY, service.getCategory());
        items.put(Service.ADDRESS, service.getAddress());
        items.put(Service.COUNT_OF_RATES, service.getCountOfRates());
        items.put(Service.CREATION_DATE, workWithTimeApi.getDateInFormatYMDHMS(new Date()));
        for (String tag : service.getTags()) {
            tagsMap.put(String.valueOf(tag.hashCode()), tag);
        }
        items.put(Service.TAGS, tagsMap);

        String serviceId = serviceRef.push().getKey();
        serviceRef = serviceRef.child(serviceId);
        serviceRef.updateChildren(items);

        service.setId(serviceId);
        addServiceInLocalStorage(service);

        for (Uri path : fpath) {
            uploadImage(path, serviceId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (service.getIsPremium()) {
            setWithPremium();
        }

        PanelBuilder panelBuilder = new PanelBuilder();
        panelBuilder.buildFooter(manager, R.id.footerAddServiceLayout);
        panelBuilder.buildHeader(manager, "Создание услуги", R.id.headerAddServiceLayout);
    }

    private void addServiceInLocalStorage(Service service) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //добавление в БД
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_ID, service.getId());
        contentValues.put(DBHelper.KEY_NAME_SERVICES, service.getName().toLowerCase());
        contentValues.put(DBHelper.KEY_RATING_SERVICES, "0");
        contentValues.put(DBHelper.KEY_MIN_COST_SERVICES, service.getCost());
        contentValues.put(DBHelper.KEY_DESCRIPTION_SERVICES, service.getDescription());
        contentValues.put(DBHelper.KEY_USER_ID, service.getUserId());
        contentValues.put(DBHelper.KEY_CATEGORY_SERVICES, service.getCategory());
        contentValues.put(DBHelper.KEY_ADDRESS_SERVICES, service.getAddress());

        database.insert(DBHelper.TABLE_CONTACTS_SERVICES, null, contentValues);

        contentValues.clear();
        contentValues.put(DBHelper.KEY_SERVICE_ID_TAGS, service.getId());
        for (String tag : service.getTags()) {
            contentValues.put(DBHelper.KEY_TAG_TAGS, tag);
            database.insert(DBHelper.TABLE_TAGS, null, contentValues);
        }

        goToMyCalendar(getString(R.string.status_worker), service.getId());
    }

    protected Boolean isFullInputs() {
        if (nameServiceInput.getText().toString().isEmpty()) return false;
        if (descriptionServiceInput.getText().toString().isEmpty()) return false;
        if (costAddServiceInput.getText().toString().isEmpty()) return false;

        return true;
    }

    private void chooseImage() {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //установка картинки на activity
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                addToScreen(bitmap, filePath);

                //serviceImage.setImageBitmap(bitmap);
                //загрузка картинки в fireStorage
                fpath.add(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri filePath, final String serviceId) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(User.Companion.getUSERS());

        if (filePath != null) {
            final String photoId = myRef.push().getKey();
            final StorageReference storageReference = firebaseStorage.getReference(SERVICE_PHOTO + "/" + photoId);
            storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadPhotos(uri.toString(), serviceId, photoId);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    private void uploadPhotos(String storageReference, String serviceId, String photoId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(User.Companion.getUSERS())
                .child(getUserId())
                .child(Service.SERVICES)
                .child(serviceId)
                .child(PHOTOS)
                .child(photoId);

        Map<String, Object> items = new HashMap<>();
        items.put(PHOTO_LINK, storageReference);
        myRef.updateChildren(items);

        Photo photo = new Photo();
        photo.setPhotoId(photoId);
        photo.setPhotoLink(storageReference);
        photo.setPhotoOwnerId(serviceId);

        addPhotoInLocalStorage(photo);
    }

    private void addPhotoInLocalStorage(Photo photo) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_ID, photo.getPhotoId());
        contentValues.put(DBHelper.KEY_PHOTO_LINK_PHOTOS, photo.getPhotoLink());
        contentValues.put(DBHelper.KEY_OWNER_ID_PHOTOS, photo.getPhotoOwnerId());

        database.insert(DBHelper.TABLE_PHOTOS, null, contentValues);
    }

    private void addToScreen(Bitmap bitmap, Uri filePath) {
        FragmentTransaction transaction = manager.beginTransaction();
        ServicePhotoElement servicePhotoElement = new ServicePhotoElement(bitmap, filePath, "add service");
        transaction.add(R.id.feedAddServiceLayout, servicePhotoElement);
        transaction.commit();
    }


    public void deleteFragment(ServicePhotoElement fr, Uri filePath) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fr);
        transaction.commit();
        fpath.remove(filePath);
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void showPremium() {
        if (isPremiumLayoutSelected) {
            premiumLayout.setVisibility(View.GONE);
            isPremiumLayoutSelected = false;
        } else {
            premiumLayout.setVisibility(View.VISIBLE);
            isPremiumLayoutSelected = true;
        }
    }

    @Override
    public void setPremium() {
        service.setIsPremium(true);
        setWithPremium();
        premiumLayout.setVisibility(View.GONE);
        premiumDate = addSevenDayPremium(WorkWithTimeApi.getDateInFormatYMDHMS(new Date()));
        attentionPremiumActivated();
    }

    @Override
    public void checkCode(final String code) {
        //проверка кода
        Query query = FirebaseDatabase.getInstance().getReference(CODES).
                orderByChild(CODE).
                equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot codesSnapshot) {
                if (codesSnapshot.getChildrenCount() == 0) {
                    attentionWrongCode();
                } else {
                    DataSnapshot userSnapshot = codesSnapshot.getChildren().iterator().next();
                    int count = userSnapshot.child(COUNT).getValue(int.class);
                    if (count > 0) {
                        setPremium();

                        String codeId = userSnapshot.getKey();

                        DatabaseReference myRef = FirebaseDatabase.getInstance()
                                .getReference(CODES)
                                .child(codeId);
                        Map<String, Object> items = new HashMap<>();
                        items.put(COUNT, count - 1);
                        myRef.updateChildren(items);
                    } else {
                        attentionOldCode();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public String addSevenDayPremium(String date) {
        long sysdateLong = WorkWithTimeApi.getMillisecondsStringDateWithSeconds(date);
        //86400000 - day * 7 day
        sysdateLong += 86400000 * 7;
        return WorkWithTimeApi.getDateInFormatYMDHMS(new Date(sysdateLong));
    }


    private void setWithPremium() {
        noPremiumText.setVisibility(View.GONE);
        premiumText.setVisibility(View.VISIBLE);
        premiumText.setEnabled(false);
    }

    private void goToMyCalendar(String status, String serviceId) {
        attentionAllDone();
        Intent intent = new Intent(this, MyCalendar.class);
        intent.putExtra(SERVICE_ID, serviceId);
        intent.putExtra(STATUS_USER_BY_SERVICE, status);

        startActivity(intent);
        finish();
    }

    private void attentionAllDone() {
        Toast.makeText(this, "Сервис успешно создан", Toast.LENGTH_SHORT).show();
    }

    private void attentionWrongCode() {
        Toast.makeText(this, "Неверно введен код", Toast.LENGTH_SHORT).show();
    }

    private void attentionOldCode() {
        Toast.makeText(this, "Код больше не действителен", Toast.LENGTH_SHORT).show();
    }

    private void attentionPremiumActivated() {
        Toast.makeText(this, "Премиум активирован", Toast.LENGTH_SHORT).show();
    }

    private void attentionMoreTenImages() {
        Toast.makeText(this, "Должно быть меньше 10 фотографий", Toast.LENGTH_SHORT).show();
    }


}