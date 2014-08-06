
function [ rerrTrain,rerrTest] = funcBagging( dataTrain, dataTest, nModel )
%bagging algo.

%% # of iteration for getting average error
numIter = 10;

% accuracy
errTrain= zeros(numIter,1);
errTest = errTrain;

% for each loop
for niter = 1:numIter
    
    % index, 0, 1
    decisionStump = zeros( nModel, 3);
    
    % randomly draw
    for nrand = 1:nModel
        randIdx = randi(size(dataTrain,1),size(dataTrain,1),1);
        curData = dataTrain(randIdx,:);
        
        % stump node 
        decisionStump(nrand,:) = stump(curData);
    end
    
    errTrain(niter) = predictBagging(dataTrain,decisionStump,nModel);
    errTest (niter) = predictBagging(dataTest,decisionStump,nModel);
end
% find average of error rate
rerrTrain = sum(errTrain)/(numIter * size(dataTrain,1));
rerrTest  = sum(errTest) /(numIter * size(dataTest,1));

end

 %% sub-function 4 select best feature
function node = stump(data_label)
    node = zeros(1,3); % index, 0, 1
    slabel = data_label(:,1);
    sdata  = data_label(:,2:end);

    % information gain, for each feature dim
    inforGain = zeros(size(sdata,2),1);

    for ncol = 1:size(sdata,2)
        % entropy
        if isempty(slabel(sdata(:,ncol)==0,1)) || isempty(slabel(sdata(:,ncol)==1,1))
            inforGain(ncol) = 0;
        else
        inforGain(ncol) = (length(find(sdata(:,ncol)==0))*entropy(slabel(sdata(:,ncol)==0,1))+...
                           length(find(sdata(:,ncol)==1))*entropy(slabel(sdata(:,ncol)==1,1)))/size(sdata,1);
        end

    end

    % select best one
    [~,r] = min(inforGain);
    node(1) = r;
    frequency = tabulate(slabel(sdata(:,r)==0,1));
    [~,r] = max(frequency(:,end));
    node(2) = frequency(r,1);
    node(3) = abs(frequency(r,1)-1);

end

%% evaluate model
function err = predictBagging(data,model,nModel)

    err = 0;
    slabel = data(:,1);
    sdata  = data(:,2:end);
    
    for nt = 1:size(slabel,1)
        pred = zeros(nModel,1);
        for nm = 1:nModel
            if sdata(nt,model(nm,1)) == 0 
                pred(nm) = model(nm,2);
            else
                pred(nm) = model(nm,3);
            end
        end
        
        n0=length(find(pred == 0)); n1=length(find(pred == 1)) ; 
        if n0 > n1 gt = 0; else gt = 1; end
        if gt ~= slabel(nt)
            err = err + 1;
        end
        
    end

end

