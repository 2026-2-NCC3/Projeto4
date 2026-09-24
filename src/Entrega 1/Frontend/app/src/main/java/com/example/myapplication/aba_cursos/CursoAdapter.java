package com.example.myapplication.aba_cursos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {

    private final List<Curso> listaCursos;

    public CursoAdapter(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);
        return new CursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        Curso curso = listaCursos.get(position);
        holder.tvNome.setText(curso.getNome());
        holder.tvCargaHoraria.setText("Carga Horária: " + curso.getCargaHoraria() + "h");
        holder.tvStatus.setText(curso.getStatus());
    }

    @Override
    public int getItemCount() {
        return listaCursos != null ? listaCursos.size() : 0;
    }

    public static class CursoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNome;
        public TextView tvCargaHoraria;
        public TextView tvStatus;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeCurso);
            tvCargaHoraria = itemView.findViewById(R.id.tvCargaHoraria);
            tvStatus = itemView.findViewById(R.id.tvStatusCurso);
        }
    }
}