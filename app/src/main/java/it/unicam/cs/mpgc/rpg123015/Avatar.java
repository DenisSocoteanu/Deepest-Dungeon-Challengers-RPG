package it.unicam.cs.mpgc.rpg123015;


import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Avatar extends Personaggio {

    ScenaLivello scena;
    Auxiliary target;
    AtomicReference<TypeOfAction> toa = new AtomicReference<>();

    public Avatar(String nome, ScenaLivello scena, XYVector dimArena)
    {
        setNome(nome);
        setIcon("/icons/AvatarSprite.png");
        setSTR(1);
        setDEX(2);
        target = new Auxiliary("targetSelection","/icons/TargetSelection.gif");

        upperLimits = dimArena;
        this.scena = scena;

        hpbar = new ProgressBar();
        hpbar.setVisible(false);

        AtomicBoolean focusTarget = new AtomicBoolean(false);
        /*scena.getScenaLivello().setOnKeyPressed(event -> {
            {
                switch(event.getCode()){
                    case W:
                        if(!focusTarget.get()) {

                            if(!(position.getY()-1 < 0)) //Controlla la posiziona, poi muove l'avatar
                            {
                                CompleteMovement(0,-1);
                                toa.set(TypeOfAction.MOVEMENT);
                            }
                            else
                                scena.PlayOOBsound();

                        }
                        else // MOVE THE TARGET
                        {
                            if(!(target.position.getY()-1 < 0))
                            {
                                target.Move(0, -1);
                                MoveTarget(target.getImage(), target.name, target.getX(), target.getY());
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        break;

                    case A:
                        if(!focusTarget.get()) {

                            if(!(position.getX()-1 < 0)) //Controlla la posiziona, poi muove l'avatar
                            {
                                CompleteMovement(-1,0);
                                toa.set(TypeOfAction.MOVEMENT);
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        else // MOVE THE TARGET
                        {
                            if(!(target.position.getX()-1 < 0))
                            {
                                target.Move(-1, 0);
                                MoveTarget(target.getImage(), target.name, target.getX(), target.getY());
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        break;

                    case S:
                        if(!focusTarget.get()) {

                            if(!(position.getY()+1 >= upperLimits.getY())) //Controlla la posiziona, poi muove l'avatar
                            {
                                CompleteMovement(0,1);
                                toa.set(TypeOfAction.MOVEMENT);
                                // notify(); non funziona perchè non c'è un thread in pausa, quando viene chiamato il thread ha già ripreso. Non posso mettere in pausa livello
                                //perchè mette in pausa tutto il gioco
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        else // MOVE THE TARGET
                        {
                            if(!(target.position.getY()+1 >= upperLimits.getY()))
                            {
                                target.Move(0, 1);
                                MoveTarget(target.getImage(), target.name, target.getX(), target.getY());
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        break;

                    case D:
                        if(!focusTarget.get()) {

                            if(!(position.getX()+1 >= upperLimits.getX())) //Controlla la posiziona, poi muove l'avatar
                            {
                                CompleteMovement(1,0);
                                toa.set(TypeOfAction.MOVEMENT);
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        else // MOVE THE TARGET
                        {
                            if(!(target.position.getX()+1 >= upperLimits.getX()))
                            {
                                target.Move(1, 0);
                                MoveTarget(target.getImage(), target.name, target.getX(), target.getY());
                            }
                            else
                                scena.PlayOOBsound();
                        }
                        break;

                    case Z: //Preme l'action key, il comportamento cambia in base allo stato del target selection
                        if(!focusTarget.get())
                        {
                            SelectTarget();
                            focusTarget.set(true);
                        }
                        else {
                            Attack();
                            focusTarget.set(false);
                        }
                        break;

                    case ENTER: //Alternativa a Z, perchè nei test continuavo a premere ENTER anzichè Z
                        if(focusTarget.get())
                        {
                            Attack();
                            focusTarget.set(false);
                        }
                        break;

                    case ESCAPE:
                        scena.ReturnToMM();
                        break;
                }
            }

        });*/

    }


    @Override
    public Action TakeAction()
    {
        return null;
    }

    public void Attack() {
        scena.RemoveEntity(target.name);

    }

    public void SelectTarget()
    {
        target.Spawn(getX(), getY());
        scena.SpawnEntity(target.getImage(), target.name, getX(), getY() );
    }

    //Sposta l'icona nella scena
    private void MoveTarget(Image i, String id, int x, int y) {
        scena.MoveEntity(i, id, x, y);

    }

    @Override
    public void Move(XYVector movement)
    {
        UpdatePosition(movement);
        MoveTarget(getIcon(), getName(), position.getX(), position.getY());
        System.out.println("POSIZIONE: " + position.getX() + ", " + position.getY());
    }

    public void ShareStats() {
        scena.getStats(maxHp,getSTR(),getDEX());
    }
}
