ARG BESU_VERSION
FROM hyperledger/besu:$BESU_VERSION
COPY --chown=besu:besu plugins/Trusted-Pool-Plugin-1.0-SNAPSHOT-all.jar /opt/besu/plugins/

ENTRYPOINT ["besu"]
