package com.example.notificao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Criar um id para o canal de notificação
    String idCanal = "canalID";
    //Criar um objeto para referenciar o gerenciar de
    //notificação do Android
    NotificationManager serviNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criaCanalNotificação();
        Button btPadrao = findViewById(R.id.btPadrao);
        Button btNovaTela = findViewById(R.id.btAbrirTela);

        btPadrao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Criar a Intent para indicar o que será aberto
                Intent it = new Intent(
                        MainActivity.this, TelaNova.class
                );

                //Transformar a Intent em PendingIntent pois
                //A intent ficará pendente/aguardando o usuário
                //clicar/tocar na notificação
                PendingIntent p = PendingIntent.getActivity(MainActivity.this, 0, it,PendingIntent.FLAG_IMMUTABLE);
                //Configuração da notificação
                NotificationCompat.Builder config = new NotificationCompat.Builder(
                        MainActivity.this, idCanal)
                        .setContentIntent(p)//o que irá abrir
                        .setAutoCancel(true)//remover a notificação
                        .setContentTitle("Título da notifição")
                        .setContentTitle("Texto dentro da notificação")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSmallIcon(R.drawable.icone_notificacao);
                //Iniciar o serviço de notificação
                NotificationManagerCompat servico =
                        NotificationManagerCompat.from(MainActivity.this);
                //Enviar a notificação para o Android exibir
                //O número 100 pode ser qualquer valor inteiro
                //ele serve para identificar a notificação se caso
                //for necessário atualizar ou remover a notificação

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                    Toast.makeText(MainActivity.this,
                            "è necessário permitir as notificações",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                servico.notify(100, config.build());

            }
        });


    }

    //Método para criar o canal de notificação
    private void criaCanalNotificação(){
        //O canal de notificação só existe no Android 8 (Oreo)
        //e nas versões após ele mas precisamos garantir que
        //a notificação funcione tanto nas versões novas e antigas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Se a versão do android é o Oreo (8) ou acima...
            // //Ocanal precisa de um nome e uma descrição
            CharSequence nomeCanal = "Canal teste";
            String descricao = "Descrição do canal teste";
            //Criar o canal de notificação
            NotificationChannel canal = new NotificationChannel(
                    idCanal, nomeCanal,
                    NotificationManager.IMPORTANCE_DEFAULT);
            //Adicionar a descrição
            canal.setDescription(descricao);
            //Cadastrar o canal no serviço de notificações do Android
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }
    }
}