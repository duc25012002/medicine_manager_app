package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemContactBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Contact;

import java.util.List;

/* CONTACT ADAPTER */
public class ProfileFragmentAdapter extends RecyclerView.Adapter<ProfileFragmentAdapter.ContactViewHolder>{

    private final List<Contact> list;
    private final IManagerContactListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerContactListener{
        void onClickItem(Contact contact);
    }

    /* KHỞI TẠO */
    public ProfileFragmentAdapter(List<Contact> mContactList, IManagerContactListener iManagerContactListener) {
        this.list = mContactList;
        this.listener = iManagerContactListener;
    }

    /* VIEW HOLDER */
    public class ContactViewHolder extends RecyclerView.ViewHolder{
        public ItemContactBinding contactBinding;

        public ContactViewHolder(ItemContactBinding contactBinding) {
            super(contactBinding.getRoot());
            this.contactBinding = contactBinding;

            /* SET SỰ KIỆN CLICK */
            contactBinding.getRoot().setOnClickListener(new IOnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    int clickedPos = getAdapterPosition();
                    if(listener != null && clickedPos != RecyclerView.NO_POSITION){
                        listener.onClickItem(list.get(clickedPos));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemContactBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_contact;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = list.get(position);
        holder.contactBinding.setContact(contact);
        holder.contactBinding.imgContact.setImageResource(contact.getImage());
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
