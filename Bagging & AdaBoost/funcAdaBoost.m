
function [ errTrain,errTest ] = funcAdaBoost( dataTrain, dataTest,nModel )
%adaBoost algo.

    % split label n data
    trainlabel = dataTrain(:,1);
    traindata  = dataTrain(:,2:end);
    %% init weight
    weight = ones(size(dataTrain,1),1)/size(dataTrain,1);
    %% others
    boostTree = zeros(nModel,3);
    errTree   = zeros(nModel,1);

    %% voted vector
    vote = errTree;

    %% error
    error = vote;
    %% for each model
    for nm = 1:nModel
        % stump node
        boostTree(nm,:) = stump(dataTrain, weight);
        % err of wrong prediction
        errpred0 = (trainlabel(traindata(:,boostTree(nm,1))==0,1)~=boostTree(nm,2));
        errpred1 = (trainlabel(traindata(:,boostTree(nm,1))==1,1)~=boostTree(nm,3));

        % weight or error predicted feature
        errw0 = weight(traindata(:,boostTree(nm,1))==0);
        errw1 = weight(traindata(:,boostTree(nm,1))==1);

        error(nm) = sum(errw0(errpred0))+sum(errw1(errpred1));
        vote(nm) = 0.5*log((1-error(nm))/error(nm)); 

        %update
        errw0(errpred0) = errw0(errpred0) * exp(vote(nm));
        errw0(~errpred0) = errw0(~errpred0) * exp(-vote(nm));
        weight(dataTrain(:,boostTree(nm,1)+1)==0) = errw0;
        errw1(errpred1) = errw1(errpred1) * exp(vote(nm));
        errw1(~errpred1) = errw1(~errpred1) * exp(-vote(nm));
        weight(dataTrain(:,boostTree(nm,1)+1)==1) = errw1;

        %normalize weights
        weight = weight/sum(weight);

    end

    errTrain = predictAdaBoost(dataTrain, boostTree,vote,nModel);
    errTest  = predictAdaBoost(dataTest , boostTree,vote,nModel);
    
    errTrain = errTrain / size(dataTrain,1);
    errTest  = errTest  / size(dataTest, 1);

end

 %% sub-function 4 select best feature
function node = stump(data_label, weight)
    node = zeros(1,3); % index, 0, 1
    slabel = data_label(:,1);
    sdata  = data_label(:,2:end);

    % information gain, for each feature dim
    inforGain = zeros(size(sdata,2),1);

    for ncol = 1:size(sdata,2)
        % entropy
        row0 = slabel(sdata(:,ncol)==0,:);
        row1 = slabel(sdata(:,ncol)==1,:);
        w0 = weight(sdata(:,ncol)==0);
        w1 = weight(sdata(:,ncol)==1);

        suml0 = sum(w0(row0(:,1)==0));
        suml1 = sum(w0(row0(:,1)==1));
        tNuml = suml0+suml1;
        suml0 = suml0/tNuml;
        suml1 = suml1/tNuml;
        
        sumr0 = sum(w1(row1(:,1)==0));
        sumr1 = sum(w1(row1(:,1)==1));
        tNumr = sumr0+sumr1;
        sumr0 = sumr0/tNumr;
        sumr1 = sumr1/tNumr;

        if suml0==0 || suml1==0
            e0 = 0;
        else    
        e0 = -suml0*log2(suml0)-suml1*log2(suml1);
        end     
        if sumr0==0 || sumr1==0
        e1 = 0;  
        else    
            e1 = -sumr0*log2(sumr0)-sumr1*log2(sumr1);
        end     
        inforGain(ncol) = sum(weight(sdata(:,ncol)==0))*e0 +...
                          sum(weight(sdata(:,ncol)==1))*e1;

    end

    % select best one
    [~,r] = min(inforGain);
    node(1) = r;
    tp0 = slabel(sdata(:,r)==0,1);
    tp1 = slabel(sdata(:,r)==1,1);
    w0 = weight(sdata(:,r)==0);
    w1 = weight(sdata(:,r)==1);
    rweight = zeros(2,2);
    rweight (1,1) = sum(w0(tp0==0));
    rweight (1,2) = sum(w0(tp0==1));
    rweight (2,1) = sum(w1(tp1==0));
    rweight (2,2) = sum(w1(tp1==1));
    [~ , index] = max(rweight, [], 2);
    node (2:3) = index - 1;
end

function err = predictAdaBoost(data, model, vote, nModel)
    err = 0;
    % split
    sdata = data(:,2:end);
    slabel= data(:,1);
    
    for nt = 1:size(sdata,1)
        predict = zeros (nModel,1);
        for nm = 1:nModel
            if sdata(nt, model(nm,1)) == 0
                predict(nm) = model(nm,2);
            elseif sdata(nt, model(nm,1)) == 1
                predict(nm) = model(nm,3);
            end
        end
        
        n0 = sum(vote(predict == 0));
        n1 = sum(vote(predict == 1));
        if n0 >= n1
            gt = 0;
        else
            gt = 1;
        end
        if gt ~= slabel(nt,1)
            err = err + 1;
        end
    end
end