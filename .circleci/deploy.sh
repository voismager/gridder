scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ~/repo/target/grid-0.0.1-SNAPSHOT.jar vlad@134.209.182.89:/home/vlad
ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null vlad@134.209.182.89 systemctl --user daemon-reload
ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null vlad@134.209.182.89 systemctl --user restart gridapp