//public class example {
//public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                                               Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        final View view = inflater.inflate(R.layout.fragment_home_page, container, false);
//
//        textView = view.findViewById(R.id.textUsername);
//        Bundle bundle = this.getArguments();
//        final String username = bundle.getString("username");
//        if (username == null || username.isEmpty()){
//            return view;
//        }
//        textView.setText(username + ", welcome to My Roommate!");
//
//        view.findViewById(R.id.postButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), PostActivity.class);
//                intent.putExtra("username", username);
//                getContext().startActivity(intent);
//            }
//        });
//
//        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), SearchActivity.class);
//                intent.putExtra("username", username);
//                getContext().startActivity(intent);
//            }
//        });
//
//        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        rootRef = FirebaseDatabase.getInstance().getReference();
//        userNameRef = rootRef.child(username);
//        userNameRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null) {
//                    GenericTypeIndicator<ArrayList<Apartment>> t = new GenericTypeIndicator<ArrayList<Apartment>>() {};
//                    ArrayList<Apartment> myApartments = dataSnapshot.getValue(t);
//                    if (myApartments != null) {
//                        LinearLayout ll = view.findViewById(R.id.listofMyPosts);
//                        ll.removeAllViews();
//                        if (myApartments.size() > 0) {
//                            TextView tv = new TextView(view.getContext());
//                            tv.setText("My posts (" + myApartments.size() + ")");
//                            tv.setTypeface(null, Typeface.BOLD_ITALIC);
//                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                            ll.addView(tv);
//                        }
//                        for (int i = 0; i < myApartments.size(); i++) {
//                            Apartment apartment = myApartments.get(i);
//                            CardView cardView = BuildCardViewForApartment(view, apartment, null);
//                            final ImageView iv = (ImageView) cardView.findViewById(R.id.imageView);
//                            if (apartment.getImageUid() != null && !apartment.getImageUid().isEmpty()) {
//                                final StorageReference imgRef = storageRef.child("images/" + apartment.getImageUid()/* + ".jpg"*/);
//                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        // Got the download URL for 'users/me/profile.png'
//                                        // Pass it to Picasso to download, show in ImageView and caching
//                                        Picasso.get().load(uri.toString()).into(iv);
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception exception) {
//
//                                        // Handle any errors
//                                        //try again to load tje image
//
//                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                Picasso.get().load(uri.toString()).into(iv);
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                iv.setVisibility(View.GONE);
//                                            }
//                                        });
//                                    }
//                                });
//                                //GlideApp.with(view.getContext()).load(imgRef.getDownloadUrl()).into(iv);
//                                //Picasso.get().load(imgRef.getDownloadUrl().toString()).into(iv);
//                            } else {
//                                iv.setVisibility(View.GONE);
//                            }
//                            ll.addView(cardView);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        rootRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null) {
//                    GenericTypeIndicator<HashMap<String, ArrayList<Apartment>>> t = new GenericTypeIndicator<HashMap<String, ArrayList<Apartment>>>() {};
//                    HashMap<String, ArrayList<Apartment>> apartments = dataSnapshot.getValue(t);
//                    if (apartments == null ){
//                        return;
//                    }
//                    LinearLayout ll = view.findViewById(R.id.listofOtherPosts);
//                    ll.removeAllViews();
//                    if (apartments !=null && apartments.size() > 0) {
//                        TextView tv = new TextView(view.getContext());
//                        tv.setText("Other posts (" + apartments.size() + ")");
//                        tv.setTypeface(null, Typeface.BOLD_ITALIC);
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                        ll.addView(tv);
//                    }
//                    for(Map.Entry<String, ArrayList<Apartment>> set : apartments.entrySet()) {
//                        if (username.equals(set.getKey())) {
//                            continue;
//                        }
//                        for (int i = 0; i < set.getValue().size(); i++) {
//                            Apartment apartment = set.getValue().get(i);
//                            CardView cardView = BuildCardViewForApartment(view, apartment, set.getKey());
//                            final ImageView iv = (ImageView) cardView.findViewById(R.id.imageView);
//                            if (apartment.getImageUid() != null && !apartment.getImageUid().isEmpty()) {
//                                final StorageReference imgRef = storageRef.child("images/" + apartment.getImageUid()/* + ".jpg"*/);
//                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        // Got the download URL for 'users/me/profile.png'
//                                        // Pass it to Picasso to download, show in ImageView and caching
//                                        Picasso.get().load(uri.toString()).into(iv);
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception exception) {
//                                        // Handle any errors
//                                    }
//                                });
//                                //GlideApp.with(view.getContext()).load(imgRef.getDownloadUrl()).into(iv);
//                                //Picasso.get().load(imgRef.getDownloadUrl().toString()).into(iv);
//                            } else {
//                                iv.setVisibility(View.GONE);
//                            }
//                            ll.addView(cardView);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        return view;
//    }
//[9:26, 15.11.2020] לב שיעור פרטי: public class HomePage extends Fragment {
//        // TODO: Rename parameter arguments, choose names that match
//        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//        private static final String ARG_PARAM1 = "param1";
//        private static final String ARG_PARAM2 = "param2";
//        TextView textView;
//        ArrayList<Apartment> apartments;
//
//        // TODO: Rename and change types of parameters
//        private String mParam1;
//        private String mParam2;
//
//        DatabaseReference rootRef, userNameRef;
//        ArrayList<Apartment> allApartments;
//
//        public HomePage() {
//            // Required empty public constructor
//        }
//
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HomePage.
//         */
//        // TODO: Rename and change types and number of parameters
//        public static HomePage newInstance(String param1, String param2) {
//            HomePage fragment = new HomePage();
//            Bundle args = new Bundle();
//            args.putString(ARG_PARAM1, param1);
//            args.putString(ARG_PARAM2, param2);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            if (getArguments() != null) {
//                mParam1 = getArguments().getString(ARG_PARAM1);
//                mParam2 = getArguments().getString(ARG_PARAM2);
//            }
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // Inflate the layout for this fragment
//            final View view = inflater.inflate(R.layout.fragment_home_page, container, false);
//
//            textView = view.findViewById(R.id.textUsername);
//            Bundle bundle = this.getArguments();
//            final String username = bundle.getString("username");
//            if (username == null || username.isEmpty()){
//                return view;
//            }
//            textView.setText(username + ", welcome to My Roommate!");
//
//            view.findViewById(R.id.postButton).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), PostActivity.class);
//                    intent.putExtra("username", username);
//                    getContext().startActivity(intent);
//                }
//            });
//
//            view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), SearchActivity.class);
//                    intent.putExtra("username", username);
//                    getContext().startActivity(intent);
//                }
//            });
//
//            final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//            rootRef = FirebaseDatabase.getInstance().getReference();
//            userNameRef = rootRef.child(username);
//            userNameRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot != null) {
//                        GenericTypeIndicator<ArrayList<Apartment>> t = new GenericTypeIndicator<ArrayList<Apartment>>() {};
//                        ArrayList<Apartment> myApartments = dataSnapshot.getValue(t);
//                        if (myApartments != null) {
//                            LinearLayout ll = view.findViewById(R.id.listofMyPosts);
//                            ll.removeAllViews();
//                            if (myApartments.size() > 0) {
//                                TextView tv = new TextView(view.getContext());
//                                tv.setText("My posts (" + myApartments.size() + ")");
//                                tv.setTypeface(null, Typeface.BOLD_ITALIC);
//                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                                ll.addView(tv);
//                            }
//                            for (int i = 0; i < myApartments.size(); i++) {
//                                Apartment apartment = myApartments.get(i);
//                                CardView cardView = BuildCardViewForApartment(view, apartment, null);
//                                final ImageView iv = (ImageView) cardView.findViewById(R.id.imageView);
//                                if (apartment.getImageUid() != null && !apartment.getImageUid().isEmpty()) {
//                                    final StorageReference imgRef = storageRef.child("images/" + apartment.getImageUid()/* + ".jpg"*/);
//                                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            // Got the download URL for 'users/me/profile.png'
//                                            // Pass it to Picasso to download, show in ImageView and caching
//                                            Picasso.get().load(uri.toString()).into(iv);
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception exception) {
//
//                                            // Handle any errors
//                                            //try again to load tje image
//
//                                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                @Override
//                                                public void onSuccess(Uri uri) {
//                                                    Picasso.get().load(uri.toString()).into(iv);
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    iv.setVisibility(View.GONE);
//                                                }
//                                            });
//                                        }
//                                    });
//                                    //GlideApp.with(view.getContext()).load(imgRef.getDownloadUrl()).into(iv);
//                                    //Picasso.get().load(imgRef.getDownloadUrl().toString()).into(iv);
//                                } else {
//                                    iv.setVisibility(View.GONE);
//                                }
//                                ll.addView(cardView);
//                            }
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//
//            rootRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot != null) {
//                        GenericTypeIndicator<HashMap<String, ArrayList<Apartment>>> t = new GenericTypeIndicator<HashMap<String, ArrayList<Apartment>>>() {};
//                        HashMap<String, ArrayList<Apartment>> apartments = dataSnapshot.getValue(t);
//                        if (apartments == null ){
//                            return;
//                        }
//                        LinearLayout ll = view.findViewById(R.id.listofOtherPosts);
//                        ll.removeAllViews();
//                        if (apartments !=null && apartments.size() > 0) {
//                            TextView tv = new TextView(view.getContext());
//                            tv.setText("Other posts (" + apartments.size() + ")");
//                            tv.setTypeface(null, Typeface.BOLD_ITALIC);
//                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                            ll.addView(tv);
//                        }
//                        for(Map.Entry<String, ArrayList<Apartment>> set : apartments.entrySet()) {
//                            if (username.equals(set.getKey())) {
//                                continue;
//                            }
//                            for (int i = 0; i < set.getValue().size(); i++) {
//                                Apartment apartment = set.getValue().get(i);
//                                CardView cardView = BuildCardViewForApartment(view, apartment, set.getKey());
//                                final ImageView iv = (ImageView) cardView.findViewById(R.id.imageView);
//                                if (apartment.getImageUid() != null && !apartment.getImageUid().isEmpty()) {
//                                    final StorageReference imgRef = storageRef.child("images/" + apartment.getImageUid()/* + ".jpg"*/);
//                                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            // Got the download URL for 'users/me/profile.png'
//                                            // Pass it to Picasso to download, show in ImageView and caching
//                                            Picasso.get().load(uri.toString()).into(iv);
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception exception) {
//                                            // Handle any errors
//                                        }
//                                    });
//                                    //GlideApp.with(view.getContext()).load(imgRef.getDownloadUrl()).into(iv);
//                                    //Picasso.get().load(imgRef.getDownloadUrl().toString()).into(iv);
//                                } else {
//                                    iv.setVisibility(View.GONE);
//                                }
//                                ll.addView(cardView);
//                            }
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//
//            return view;
//        }
//
//        public static CardView BuildCardViewForApartment(View view, Apartment apartment, String username) {
//
//            CardView cardView = new CardView(view.getContext());
//            cardView.setLayoutParams(
//                    new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
//            cardView.setPadding(8, 0, 0, 0);
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
//            cardViewMarginParams.setMargins(8, 16, 8, 16);
//            cardView.requestLayout();
//            LinearLayout llCardView = new LinearLayout(view.getContext());
//            llCardView.setOrientation(LinearLayout.VERTICAL);
//            llCardView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            // Image
//            ImageView iv = new ImageView(view.getContext());
//            iv.setId(R.id.imageView);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
//            iv.setLayoutParams(layoutParams);
//            llCardView.addView(iv);
//            // Location
//            TextView tv = new TextView(view.getContext());
//            tv.setPadding(4, 0, 0, 0);
//            String title = apartment.getLocation();
//            if (username != null) {
//                title += ", by " + username;
//            }
//            tv.setText(title);
//            tv.setTypeface(null, Typeface.BOLD_ITALIC);
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            llCardView.addView(tv);
//            // Price
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            tv.setText("Price: " + apartment.getPrice());
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Is Furnished
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            if (apartment.getIsFurnished()) {
//                tv.setText("Already furnished!");
//            } else {
//                tv.setText("Is NOT furnished!");
//            }
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Is Pet Allowed
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            if (apartment.getIsPetAllowed()) {
//                tv.setText("Pets are allowed!");
//            } else {
//                tv.setText("Pets are NOT allowed!");
//            }
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Is smoking allowed
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            if (apartment.getIsSmokingAllowed()) {
//                tv.setText("Smoking is allowed!");
//            } else {
//                tv.setText("Smoking is NOT allowed!");
//            }
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Kosherout
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            if (apartment.getKosherout()) {
//                tv.setText("Kosherout: yes");
//            } else {
//                tv.setText("Kosherout: no");
//            }
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Gender
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            String g = apartment.getGender();
//            tv.setText("Gender: " + ((g == null || g.isEmpty()) ? "mixed" : g));
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Shared room
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            if (apartment.getIsSharedRoom()) {
//                tv.setText("Shared room!");
//            } else {
//                tv.setText("Whole flat!");
//            }
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Public transport
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            if (apartment.getIsPublicTransport()) {
//                tv.setText("Public transport is available!");
//            } else {
//                tv.setText("Public transport is NOT available!");
//            }
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Email
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            tv.setText("Email: " + apartment.getEmail());
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            // Phone number
//            tv = new TextView(view.getContext());
//            tv.setPadding(6, 0, 0, 0);
//            tv.setText("Phone number: " + apartment.getPhoneNumber());
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            llCardView.addView(tv);
//            cardView.addView(llCardView);
//            return cardView;
//        }
//    }
//}
