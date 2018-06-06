package com.coffeepotion.flappyghost;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyGhost extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] ghosts;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private Texture gameOver;
    private Random numeroRandomico;
    private BitmapFont fonte;
    private BitmapFont mensagem;
    private Circle ghostCirculo;
    private Rectangle retanguloCanoTopo;
    private Rectangle retanguloCanoBaixo;
    //private ShapeRenderer shape;

    //Atributos de configuracao
    private float larguraDispositivo;
    private float alturaDispositivo;
    private int estadoJogo=0;// 0-> jogo não iniciado 1-> jogo iniciado 2-> Game Over
    private int pontuacao=0;

    private float variacao = 0;
    private float velocidadeQueda=0;
    private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal;
    private float deltaTime;
    private float alturaEntreCanosRandomica;
    private boolean marcouPonto=false;

    //Câmera
    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 768;
    private final float VIRTUAL_HEIGHT = 1024;

    //Valores Padrao
    private float espacoEntreCanos = 250;
    private float velocidadeHorizontalCanos = 400;

    @Override
    public void create () {
        batch = new SpriteBatch();
        numeroRandomico = new Random();
        ghostCirculo = new Circle();
        fonte = new BitmapFont(Gdx.files.internal("exo-large.fnt"));
        fonte.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(1);

        mensagem = new BitmapFont(Gdx.files.internal("exo-medium.fnt"));
        mensagem.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(1);


        ghosts = new Texture[3];
        for (int i = 0; i < ghosts.length; i++){
            ghosts[i] = new Texture("ghost"+(i+1)+".png");
        }

        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo.png");
        canoTopo = new Texture("cano_topo.png");
        gameOver = new Texture("game_over.png");

        /**********************************************
         * Configuração da câmera
        * */
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        larguraDispositivo = VIRTUAL_WIDTH;
        alturaDispositivo  = VIRTUAL_HEIGHT;

        posicaoInicialVertical = alturaDispositivo / 2;
        posicaoMovimentoCanoHorizontal = larguraDispositivo;
    }

    @Override
    public void render () {

        camera.update();

        // Limpar frames anteriores
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;
        if (variacao > 2) variacao = 0;

        if( estadoJogo == 0 ){//Não iniciado

            if( Gdx.input.justTouched() ){
                estadoJogo = 1;
            }

        }else {//Iniciado

            velocidadeQueda++;
            if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

            if( estadoJogo == 1 ){//iniciado

                posicaoMovimentoCanoHorizontal -= deltaTime * velocidadeHorizontalCanos;

                if (Gdx.input.justTouched()) {
                    velocidadeQueda = -15;
                }

                //Verifica se o cano saiu inteiramente da tela
                if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                    alturaEntreCanosRandomica = numeroRandomico.nextInt(400) - 200;
                    marcouPonto = false;
                }

                //Verifica pontuação
                if(posicaoMovimentoCanoHorizontal < 120 ){
                    if( !marcouPonto ){
                        pontuacao++;
                        marcouPonto = true;
                    }
                }

            }else{// Game Over
                //Zerar o valores padrões
                if( Gdx.input.justTouched() ){
                    estadoJogo = 0;
                    velocidadeQueda = 0;
                    pontuacao = 0;
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                    posicaoInicialVertical = alturaDispositivo / 2;
                }

            }


        }

        //Configurar dados de projeção da câmera
        batch.setProjectionMatrix( camera.combined );

        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(ghosts[(int) variacao], 120, posicaoInicialVertical);
        fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

        if( estadoJogo == 2 ) {
            mensagem.draw(batch, "Toque para reiniciar!", larguraDispositivo / 2 - 230, alturaDispositivo / 2 - gameOver.getHeight());
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
        }

        batch.end();

        ghostCirculo.set(120 + ghosts[0].getWidth() / 2, posicaoInicialVertical + ghosts[0].getHeight() / 2, ghosts[0].getWidth() / 2);
        retanguloCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica,
                canoBaixo.getWidth(), canoBaixo.getHeight()
        );

        retanguloCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica,
                canoTopo.getWidth(), canoTopo.getHeight()
        );


        //Teste de colisão
        if( Intersector.overlaps( ghostCirculo, retanguloCanoBaixo ) || Intersector.overlaps(ghostCirculo, retanguloCanoTopo)
                || posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDispositivo ){
            //Gdx.app.log("Colisão", "Houve colisão");
            estadoJogo = 2;
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
